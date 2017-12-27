package com.tkpd.library.ui.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class TouchViewPager extends ViewPager {
    private boolean AllowPageSwitching = true;

    public TouchViewPager(Context context) {
        super(context);
    }

    public TouchViewPager(Context context, AttributeSet attrs){
        super(context, attrs);
    }

    public void SetAllowPageSwitching(boolean permission){
        AllowPageSwitching = permission;
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

}
