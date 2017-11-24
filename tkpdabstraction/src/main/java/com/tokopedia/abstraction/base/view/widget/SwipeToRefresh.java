package com.tokopedia.abstraction.base.view.widget;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

import com.tokopedia.abstraction.R;

/**
 * Created by Nisie on 18/06/15.
 */
public class SwipeToRefresh extends SwipeRefreshLayout {

    private int mTouchSlop;
    private float mPrevX;
    private boolean mDeclined;

    public SwipeToRefresh(Context context) {
        super(context);
        setColorSchemeResources(R.color.tkpd_dark_green);
    }

    public SwipeToRefresh(Context context, AttributeSet attrs) {
        super(context,attrs);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        setColorSchemeResources(R.color.tkpd_dark_green);
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
}