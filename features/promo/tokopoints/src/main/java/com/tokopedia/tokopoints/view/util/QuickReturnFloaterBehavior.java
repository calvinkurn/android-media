package com.tokopedia.tokopoints.view.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.tokopedia.tokopoints.R;

public class QuickReturnFloaterBehavior extends CoordinatorLayout.Behavior<View> {

    private static final int MIN_SCROLL_PX = 4;
    private static final int TIME_TO_RETURN_MS = 500;
    private boolean mIsScrollStarted;

    public QuickReturnFloaterBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void slideUp(View child) {
        if (child.getVisibility() != View.VISIBLE) {
            Animation bottomUp = AnimationUtils.loadAnimation(child.getContext(),
                    R.anim.tp_bottom_up);
            child.startAnimation(bottomUp);
            child.setVisibility(View.VISIBLE);
        }

    }

    private void slideDown(View child) {
        if (child.getVisibility() != View.GONE) {
            Animation slideDown = AnimationUtils.loadAnimation(child.getContext(),
                    R.anim.tp_bottom_down);
            child.startAnimation(slideDown);
            child.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout,
                                       View child, @NonNull
                                               View directTargetChild, @NonNull View target,
                                       int axes, int type) {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child,
                               @NonNull View target, int dxConsumed, int dyConsumed,
                               int dxUnconsumed, int dyUnconsumed,
                               @ViewCompat.NestedScrollType int type) {
        child.post(() -> {
            if (Math.abs(dyConsumed) >= MIN_SCROLL_PX) {
                slideDown(child);
                mIsScrollStarted = true;
            }
        });
    }


    public void onStopNestedScroll(@NonNull CoordinatorLayout coordinatorLayout,
                                   @NonNull View child, @NonNull View target, @ViewCompat.NestedScrollType int type) {
        child.postDelayed(() -> {
            if (mIsScrollStarted) {
                slideUp(child);
                mIsScrollStarted = false;
            }
        }, TIME_TO_RETURN_MS);
    }

}
