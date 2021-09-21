package com.tokopedia.abstraction.base.view.widget;

import android.content.Context;
import android.os.Build;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;

import com.tokopedia.abstraction.R;

/**
 * Created by Nisie on 18/06/15.
 * <p>
 * 30/11/2017 add setDistanceToTriggerSync to improve swipe to refresh distance,
 * so user will not accidentally refresh when scroll down
 */
public class SwipeToRefresh extends SwipeRefreshLayout implements ViewTreeObserver
        .OnGlobalLayoutListener {

    static float MAX_SWIPE_DISTANCE_FACTOR = 0.5f;
    private int DEFAULT_REFRESH_TRIGGER_DISTANCE = 300;

    private int mTouchSlop;
    private float mPrevX;
    private boolean mDeclined;

    public SwipeToRefresh(Context context) {
        super(context);
        init(context);
    }

    public SwipeToRefresh(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        setColorSchemeResources(com.tokopedia.unifyprinciples.R.color.Unify_G400);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPrevX = MotionEvent.obtain(event).getX();
                mDeclined = false;
                break;

            case MotionEvent.ACTION_MOVE:
                final float eventX = event.getX();
                float xDiff = Math.abs(eventX - mPrevX);

                if (mDeclined || xDiff > mTouchSlop) {
                    mDeclined = true; // Memorize
                    return false;
                }
        }

        return super.onInterceptTouchEvent(event);
    }

    private boolean mMeasured = false;
    private boolean mPreMeasureRefreshing = false;

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (!mMeasured) {
            mMeasured = true;
            setRefreshing(mPreMeasureRefreshing);
        }
    }


    @Override
    public void setRefreshing(boolean refreshing) {
        if (mMeasured) {
            super.setRefreshing(refreshing);
        } else {
            mPreMeasureRefreshing = refreshing;
        }
    }

    @Override
    public void onGlobalLayout() {
        final DisplayMetrics metrics = getResources().getDisplayMetrics();
        Float mDistanceToTriggerSync = Math.min(
                ((View) getParent()).getHeight() * MAX_SWIPE_DISTANCE_FACTOR,
                DEFAULT_REFRESH_TRIGGER_DISTANCE * metrics.density);
        if (Build.VERSION.SDK_INT >= 21) {
            this.setDistanceToTriggerSync(Math.round(mDistanceToTriggerSync));
        }

        // Only needs to be done once so remove listener.
        ViewTreeObserver obs = getViewTreeObserver();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            obs.removeOnGlobalLayoutListener(this);
        } else {
            //noinspection deprecation
            obs.removeGlobalOnLayoutListener(this);
        }
    }

    public void setSwipeDistance() {
        this.getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    public void setSwipeDistance(int triggerDistance) {
        this.DEFAULT_REFRESH_TRIGGER_DISTANCE = triggerDistance;
        this.getViewTreeObserver().addOnGlobalLayoutListener(this);
    }
}