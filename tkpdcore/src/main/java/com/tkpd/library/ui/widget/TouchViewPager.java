package com.tkpd.library.ui.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * refer {@link com.tokopedia.abstraction.base.view.widget.TouchViewPager}
 */
@Deprecated
public class TouchViewPager extends ViewPager {
    private boolean AllowPageSwitching = true;
    private boolean isSmoothScroll = true;

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
