package com.tokopedia.tokopoints.view.util;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.tokopedia.tokopoints.R;

public class QuickReturnFloaterBehavior extends CoordinatorLayout.Behavior<View> {

    public QuickReturnFloaterBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void slideUp(View child) {
        Animation bottomUp = AnimationUtils.loadAnimation(child.getContext(),
                R.animator.bottom_up);
        child.startAnimation(bottomUp);
        child.setVisibility(View.VISIBLE);

    }

    private void slideDown(View child) {
        Animation slideDown = AnimationUtils.loadAnimation(child.getContext(),
                R.animator.bottom_down);
        child.startAnimation(slideDown);
        child.setVisibility(View.GONE);
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
        child.post(new Runnable() {
            @Override
            public void run() {
                slideDown(child);
            }
        });
    }


    public void onStopNestedScroll(@NonNull CoordinatorLayout coordinatorLayout,
                                   @NonNull View child, @NonNull View target, @ViewCompat.NestedScrollType int type) {
        child.postDelayed(new Runnable() {
            @Override
            public void run() {
                slideUp(child);
            }
        }, 500);
    }
}
