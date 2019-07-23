package com.tokopedia.design.viewpager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 *
 * Created by Tokopedia 05 on 7/11/2016.
 */
public class  WrapContentViewPager extends ViewPager {
    private View currentView;

    public WrapContentViewPager(Context context) {
        super(context);
    }

    public WrapContentViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        if (currentView == null) {
            int heightMeasureSpecTemp = heightMeasureSpec;
            View child = getChildAt(getCurrentItem());
            if (child != null) {
                child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                int h = child.getMeasuredHeight();
                heightMeasureSpecTemp = MeasureSpec.makeMeasureSpec(h, MeasureSpec.EXACTLY);
            }
            super.onMeasure(widthMeasureSpec, heightMeasureSpecTemp);
            return;
        }
        int height = 0;
        currentView.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        int h = currentView.getMeasuredHeight();
        if (h > height) height = h;
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
    public void measureCurrentView(View currentView) {
        this.currentView = currentView;
        requestLayout();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (getCurrentItem() == 0 && getChildCount() == 0) {
            return false;
        } else {
            return super.onTouchEvent(ev);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (getCurrentItem() == 0 && getChildCount() == 0) {
            return false;
        } else {
            return super.onInterceptTouchEvent(ev);
        }
    }
}
