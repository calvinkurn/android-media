package com.tokopedia.dilayanitokopedia.home.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class ToggleableSwipeRefreshLayout extends SwipeRefreshLayout {

    private boolean canChildScrollUp = false;
    private int mTouchSlop;
    private float mPrevX;

    public ToggleableSwipeRefreshLayout(@NonNull Context context) {
        super(context);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public ToggleableSwipeRefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return super.onInterceptTouchEvent(event);
    }

    public void setCanChildScrollUp(boolean canChildScrollUp) {
        this.canChildScrollUp = canChildScrollUp;
    }

    @Override
    public boolean canChildScrollUp() {
        return canChildScrollUp;
    }
}