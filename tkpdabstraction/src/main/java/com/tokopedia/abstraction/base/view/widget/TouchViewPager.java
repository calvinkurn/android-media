package com.tokopedia.abstraction.base.view.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class TouchViewPager extends ViewPager {

    private boolean allowPageSwitching = true;

    public TouchViewPager(Context context) {
        super(context);
    }

    public TouchViewPager(Context context, AttributeSet attrs){
        super(context, attrs);
    }

    public void setAllowPageSwitching(boolean allowPageSwitching) {
        this.allowPageSwitching = allowPageSwitching;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        return allowPageSwitching &&
                (getCurrentItem() != 0 ||
                        getChildCount() != 0) &&
                super.onInterceptTouchEvent(arg0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        return allowPageSwitching &&
                (getCurrentItem() != 0 ||
                        getChildCount() != 0) &&
                super.onTouchEvent(arg0);
    }

}
