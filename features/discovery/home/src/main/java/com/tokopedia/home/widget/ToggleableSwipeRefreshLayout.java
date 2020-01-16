package com.tokopedia.home.widget;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

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
        // help handle swipe between viewPager and swipe
        // https://stackoverflow.com/a/23989911/1233979
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPrevX = MotionEvent.obtain(event).getX();
                break;

            case MotionEvent.ACTION_MOVE:
                final float eventX = event.getX();
                float xDiff = Math.abs(eventX - mPrevX);

                if (xDiff > mTouchSlop) {
                    return false;
                }
        }
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