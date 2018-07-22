package com.tokopedia.imagepicker.common.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by hendry on 19/04/18.
 */

public class NonSwipeableViewPager extends ViewPager {

    private boolean canSwipe = false;

    public NonSwipeableViewPager(Context context) {
        super(context);
    }

    public NonSwipeableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        //reference for try catch: https://stackoverflow.com/questions/6919292/pointerindex-out-of-range-android-multitouch
        try {
            // Never allow swiping to switch between pages
            return canSwipe && super.onInterceptTouchEvent(arg0);
        }catch (Exception e) {
            return false;
        }
    }

    public void setCanSwipe(boolean canSwipe) {
        this.canSwipe = canSwipe;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // reference for try catch: https://stackoverflow.com/questions/6919292/pointerindex-out-of-range-android-multitouch
        // Never allow swiping to switch between pages
        try {
            return canSwipe && super.onTouchEvent(event);
        } catch (Exception e) {
            return false;
        }
    }
}
