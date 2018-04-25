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
        // Never allow swiping to switch between pages
        return canSwipe && super.onInterceptTouchEvent(arg0);
    }

    public void setCanSwipe(boolean canSwipe) {
        this.canSwipe = canSwipe;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Never allow swiping to switch between pages
        return canSwipe && super.onTouchEvent(event);
    }
}
