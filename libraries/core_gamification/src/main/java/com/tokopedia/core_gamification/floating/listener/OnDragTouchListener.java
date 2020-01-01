package com.tokopedia.core_gamification.floating.listener;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

/**
 * Created by hendry on 29/03/18.
 */

public class OnDragTouchListener implements View.OnTouchListener {

    private int halfScaledTouchSlop;

    /**
     * Callback used to indicate when the drag is finished
     */
    public interface OnDragActionListener {
        /**
         * Called when drag event is started
         *
         * @param view The view dragged
         */
        void onDragStart(View view);

        /**
         * Called when drag event is completed
         *
         * @param view The view dragged
         */
        void onDragEnd(View view);
    }

    private View mView;
    private View mParent;
    private boolean isDragging;

    private int width;
    private float maxLeft;
    private float maxRight;
    private float startX;
    private float dX;

    private boolean hasMoved;

    private int height;
    private float maxTop;
    private float maxBottom;
    private float startY;
    private float dY;

    private OnDragActionListener mOnDragActionListener;

    public OnDragTouchListener(View view) {
        this(view, (View) view.getParent(), null);
    }

    public OnDragTouchListener(View view, View parent) {
        this(view, parent, null);
    }

    public OnDragTouchListener(View view, OnDragActionListener onDragActionListener) {
        this(view, (View) view.getParent(), onDragActionListener);
    }

    public OnDragTouchListener(View view, View parent, OnDragActionListener onDragActionListener) {
        initListener(view, parent);
        setOnDragActionListener(onDragActionListener);
        ViewConfiguration viewConfiguration = ViewConfiguration.get(view.getContext());
        halfScaledTouchSlop = viewConfiguration.getScaledTouchSlop() / 2;
    }

    public void setOnDragActionListener(OnDragActionListener onDragActionListener) {
        mOnDragActionListener = onDragActionListener;
    }

    public void initListener(View view, View parent) {
        mView = view;
        mParent = parent;
        isDragging = false;
    }

    public void updateBounds() {
        updateViewBounds();
        updateParentBounds();
    }

    public void updateViewBounds() {
        width = mView.getWidth();
        height = mView.getHeight();
    }

    public void updateParentBounds() {
        maxLeft = 0;
        maxRight = maxLeft + mParent.getWidth();

        maxTop = 0;
        maxBottom = maxTop + mParent.getHeight();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (isDragging) {
            float[] bounds = new float[4];
            // LEFT
            bounds[0] = event.getRawX() + dX;
            if (bounds[0] < maxLeft) {
                bounds[0] = maxLeft;
            }
            // RIGHT
            bounds[2] = bounds[0] + width;
            if (bounds[2] > maxRight) {
                bounds[2] = maxRight;
                bounds[0] = bounds[2] - width;
            }
            // TOP
            bounds[1] = event.getRawY() + dY;
            if (bounds[1] < maxTop) {
                bounds[1] = maxTop;
            }
            // BOTTOM
            bounds[3] = bounds[1] + height;
            if (bounds[3] > maxBottom) {
                bounds[3] = maxBottom;
                bounds[1] = bounds[3] - height;
            }

            switch (event.getAction()) {
                case MotionEvent.ACTION_CANCEL:
                    onDragFinish();
                    break;
                case MotionEvent.ACTION_UP:
                    if (!hasMoved) {
                        v.performClick();
                    }
                    onDragFinish();
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (!hasMoved &&
                            (Math.abs(mView.getX() - bounds[0]) >= halfScaledTouchSlop ||
                            Math.abs(mView.getY() - bounds[1]) >= halfScaledTouchSlop) ) {
                        hasMoved = true;
                    }
                    if (hasMoved) {
                        mView.setX(bounds[0]);
                        mView.setY(bounds[1]);
                    }
                    break;
            }
            return true;
        } else {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    isDragging = true;
                    hasMoved = false;

                    updateBounds();

                    startX = v.getX();
                    dX = startX - event.getRawX();
                    startY = v.getY();
                    dY = startY - event.getRawY();
                    if (mOnDragActionListener != null) {
                        mOnDragActionListener.onDragStart(mView);
                    }
                    mView.getParent().requestDisallowInterceptTouchEvent(true);
                    return true;
            }
        }
        return false;
    }

    private void onDragFinish() {
        if (mOnDragActionListener != null) {
            mOnDragActionListener.onDragEnd(mView);
        }
        mView.getParent().requestDisallowInterceptTouchEvent(false);

        hasMoved = false;
        dX = 0;
        dY = 0;
        startX = 0;
        startY = 0;
        isDragging = false;
    }
}
