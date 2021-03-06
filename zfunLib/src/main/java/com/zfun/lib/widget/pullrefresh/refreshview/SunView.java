package com.zfun.lib.widget.pullrefresh.refreshview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import com.zfun.lib.util.AndroidUtil;
import com.zfun.lib.widget.pullrefresh.PullRefreshLayout;
import com.zfun.lib.widget.pullrefresh.RefreshHeader;

public class SunView extends View implements RefreshHeader {

  private final static float REFRESH_HEIGHT = 120;// dp

  private SunRefreshDrawable mDrawable;

  private boolean mHasDoRefresh = false;

  public SunView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  public SunView(Context context) {
    super(context);
    init();
  }

  public SunView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  private void init() {
    final int refreshHeight = (int) AndroidUtil.convertDpToPixel(getContext(), REFRESH_HEIGHT);
    mDrawable = new SunRefreshDrawable(getContext(), this, refreshHeight);

    mDrawable.offsetTopAndBottom(refreshHeight + 80);
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    int height = mDrawable.getTotalDragDistance() * 6 / 4;
    heightMeasureSpec = MeasureSpec.makeMeasureSpec(height + getPaddingTop() + getPaddingBottom(),
        MeasureSpec.EXACTLY);
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
  }

  @Override protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
    int pl = getPaddingLeft();
    int pt = getPaddingTop();
    mDrawable.setBounds(pl, pt, pl + right - left, pt + bottom - top);
  }

  @Override protected void onDraw(Canvas canvas) {
    mDrawable.draw(canvas);
  }

  @Override public void invalidateDrawable(Drawable dr) {
    if (dr == mDrawable) {
      invalidate();
    } else {
      super.invalidateDrawable(dr);
    }
  }

  @Override public int createTrigRefreshHeight() {
    return ViewGroup.LayoutParams.WRAP_CONTENT;
  }

  @Override public void onBeginRefresh() {
    mDrawable.start();
    //mHasDoRefresh = true;
  }

  @Override public void onStopRefresh() {
  }

  @Override public void onStopRefreshComplete() {
    mDrawable.stop();
    invalidate();
  }

  @Override public void onContentViewScrollDistance(int distance, PullRefreshLayout.State state) {
    if(state == PullRefreshLayout.State.REFRESHING){
      return;
    }
    if (!mHasDoRefresh) {
      float percent = (float) distance / getHeight();
      mDrawable.setPercent(percent);
      invalidate();
    }
  }

  @Override public void onContentViewBeginScroll() {
    mDrawable.resetOriginals();
    invalidate();
    mHasDoRefresh = false;
  }

  @Override public void onContentViewEndScroll() {
    mDrawable.stop();
    invalidate();
  }
}
