package com.tokopedia.challenges.view.adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.tokopedia.abstraction.R;

public class TouchViewPager extends ViewPager {
    private boolean allowPageSwitching = true;

    public TouchViewPager(Context context) {
        super(context);
    }

    public TouchViewPager(Context context, AttributeSet attrs){
        super(context, attrs);
        applyAttrs(attrs);
    }

    private void applyAttrs(AttributeSet attrs) {
        TypedArray styledAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.TouchViewPager);
        try {
            allowPageSwitching = styledAttributes.getBoolean(R.styleable.TouchViewPager_can_swipe, true);
        } finally {
            styledAttributes.recycle();
        }
    }

    public void SetAllowPageSwitching(boolean permission){
        allowPageSwitching = permission;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        //reference for try catch: https://stackoverflow.com/questions/6919292/pointerindex-out-of-range-android-multitouch
        try {
            // Never allow swiping to switch between pages
            return allowPageSwitching && !(getCurrentItem() == 0 && getChildCount() == 0) && super.onInterceptTouchEvent(arg0);
        }catch (Exception e) {
            return false;
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        //reference for try catch: https://stackoverflow.com/questions/6919292/pointerindex-out-of-range-android-multitouch
        try {
            // Never allow swiping to switch between pages
            return allowPageSwitching && !(getCurrentItem() == 0 && getChildCount() == 0) && super.onTouchEvent(arg0);
        }catch (Exception e) {
            return false;
        }
    }

}
