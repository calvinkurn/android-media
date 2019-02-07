package com.tokopedia.design.component.ticker;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Rizky on 07/08/18.
 */
public class TouchViewPager2 extends ViewPager {

    private View currentView;
    private boolean AllowPageSwitching = true;
    private boolean isSmoothScroll = true;

    public TouchViewPager2(Context context) {
        super(context);
    }

    public TouchViewPager2(Context context, AttributeSet attrs){
        super(context, attrs);
    }

    public void SetAllowPageSwitching(boolean permission){
        AllowPageSwitching = permission;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (currentView == null) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        int height = 0;
        currentView.measure(widthMeasureSpec, View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        int h = currentView.getMeasuredHeight();
        if (h > height) height = h;
        heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void measureCurrentView(View currentView) {
        this.currentView = currentView;
        requestLayout();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        if(AllowPageSwitching) {
            if (getCurrentItem() == 0 && getChildCount() == 0) {
                return false;
            } else {
                return super.onInterceptTouchEvent(arg0);
            }
        } else {
            return false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        if(AllowPageSwitching) {
            if (getCurrentItem() == 0 && getChildCount() == 0) {
                return false;
            } else {
                return super.onTouchEvent(arg0);
            }
        } else {
            return false;
        }
    }

    public void setSmoothScroll(boolean smoothScroll) {
        isSmoothScroll = smoothScroll;
    }

    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item, isSmoothScroll);
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        super.setCurrentItem(item, isSmoothScroll);
    }

}
