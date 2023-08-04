package com.tokopedia.editor.ui.gesture.java;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.tokopedia.editor.ui.gesture.listener.OnGestureControl;
import com.tokopedia.editor.ui.gesture.listener.OnMultiTouchListener;
import com.tokopedia.editor.ui.model.AddTextModel;
import com.tokopedia.editor.ui.widget.GridGuidelineView;

public class MultiTouchListener implements View.OnTouchListener {

    private static final int INVALID_POINTER_ID = -1;
    private final GestureDetector gestureListener;
    boolean isRotateEnabled = true;
    boolean isTranslateEnabled = true;
    boolean isScaleEnabled = true;
    float minimumScale = 0.2f;
    float maximumScale = 10.0f;
    private int activePointerId = INVALID_POINTER_ID;
    private float prevX, prevY;

    private ScaleGestureDetector scaleGestureDetector;
    private OnMultiTouchListener onMultiTouchListener;
    private OnGestureControl onGestureControl;
    boolean isTextPinchZoomable;

    private GridGuidelineView gridGuidelineView;

    public MultiTouchListener(Context context, GridGuidelineView guideline) {
        isTextPinchZoomable = true;
        scaleGestureDetector = new ScaleGestureDetector(new ScaleGestureListener(this));
        gestureListener = new GestureDetector(context, new GestureListener());

        gridGuidelineView = guideline;
    }

    private float adjustAngle(float degrees) {
        if (degrees > 180.0f) {
            degrees -= 360.0f;
        } else if (degrees < -180.0f) {
            degrees += 360.0f;
        }

        return degrees;
    }

    void move(View view, AddTextModel info) {
        computeRenderOffset(view, info.getPivotX(), info.getPivotY());
        adjustTranslation(view, info.getDeltaX(), info.getDeltaY());

        float scale = view.getScaleX() * info.getDeltaScale();
        scale = Math.max(info.getMinScale(), Math.min(info.getMaxScale(), scale));
        view.setScaleX(scale);
        view.setScaleY(scale);

        float rotation = adjustAngle(view.getRotation() + info.getDeltaAngle());
        view.setRotation(rotation);
    }

    private void adjustTranslation(View view, float deltaX, float deltaY) {
        float[] deltaVector = {deltaX, deltaY};
        view.getMatrix().mapVectors(deltaVector);
        view.setTranslationX(view.getTranslationX() + deltaVector[0]);
        view.setTranslationY(view.getTranslationY() + deltaVector[1]);
    }

    private void computeRenderOffset(View view, float pivotX, float pivotY) {
        if (view.getPivotX() == pivotX && view.getPivotY() == pivotY) {
            return;
        }

        float[] prevPoint = {0.0f, 0.0f};
        view.getMatrix().mapPoints(prevPoint);

        view.setPivotX(pivotX);
        view.setPivotY(pivotY);

        float[] currPoint = {0.0f, 0.0f};
        view.getMatrix().mapPoints(currPoint);

        float offsetX = currPoint[0] - prevPoint[0];
        float offsetY = currPoint[1] - prevPoint[1];

        view.setTranslationX(view.getTranslationX() - offsetX);
        view.setTranslationY(view.getTranslationY() - offsetY);
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        scaleGestureDetector.onTouchEvent(view, event);
        gestureListener.onTouchEvent(event);

        if (!isTranslateEnabled) {
            return true;
        }

        int action = event.getAction();

        int x = (int) event.getRawX();
        int y = (int) event.getRawY();

        switch (action & event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                prevX = event.getX();
                prevY = event.getY();
                activePointerId = event.getPointerId(0);
                view.bringToFront();

                break;
            case MotionEvent.ACTION_MOVE:
                int pointerIndexMove = event.findPointerIndex(activePointerId);
                if (pointerIndexMove != -1) {
                    float currX = event.getX(pointerIndexMove);
                    float currY = event.getY(pointerIndexMove);
                    if (!scaleGestureDetector.isInProgress()) {
                        adjustTranslation(view, currX - prevX, currY - prevY);
                    }

                    // Calculate distances from the center of the parent
                    float centerX = view.getX() + view.getWidth() / 2f;
                    float centerY = view.getY() + view.getHeight() / 2f;
                    float parentCenterX = ((View) view.getParent()).getWidth() / 2f;
                    float parentCenterY = ((View) view.getParent()).getHeight() / 2f;

                    // Check if the TextView is close to the vertical center of the parent
                    boolean isAlignedWithCenterX = Math.abs(centerX - parentCenterX) <= 20f;

                    // Check if the TextView is close to the horizontal center of the parent
                    boolean isAlignedWithCenterY = Math.abs(centerY - parentCenterY) <= 20f;

                    // Snap effect for horizontal alignment
                    if (isAlignedWithCenterX) {
                        float newX = parentCenterX - view.getWidth() / 2f;
                        view.setX(newX);
                    }

                    // Snap effect for vertical alignment
                    if (isAlignedWithCenterY) {
                        float newY = parentCenterY - view.getHeight() / 2f;
                        view.setY(newY);
                    }

                    // Show/hide vertical and horizontal guidelines based on alignment
                    gridGuidelineView.setShowVerticalLine(isAlignedWithCenterX);
                    gridGuidelineView.setShowHorizontalLine(isAlignedWithCenterY);
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                activePointerId = INVALID_POINTER_ID;
                break;
            case MotionEvent.ACTION_UP:
                activePointerId = INVALID_POINTER_ID;

                if (isAlignedWithCenterX(view)) {
                    view.setX(((View) view.getParent()).getWidth() / 2f - view.getWidth() / 2f);
                }

                if (isAlignedWithCenterY(view)) {
                    view.setY(((View) view.getParent()).getHeight() / 2f - view.getHeight() / 2f);
                }

                gridGuidelineView.setShowVerticalLine(false);
                gridGuidelineView.setShowHorizontalLine(false);
                break;
            case MotionEvent.ACTION_POINTER_UP:
                int pointerIndexPointerUp = (action & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
                int pointerId = event.getPointerId(pointerIndexPointerUp);
                if (pointerId == activePointerId) {
                    int newPointerIndex = pointerIndexPointerUp == 0 ? 1 : 0;
                    prevX = event.getX(newPointerIndex);
                    prevY = event.getY(newPointerIndex);
                    activePointerId = event.getPointerId(newPointerIndex);
                }
                break;
        }
        return true;
    }

    private boolean isAlignedWithCenterX(View view) {
        float parentWidth = ((View) view.getParent()).getWidth();
        float centerX = parentWidth / 2;
        float viewX = (view.getX() + view.getWidth()) / 2;
        return Math.abs(centerX - viewX) <= 20;
    }

    private boolean isAlignedWithCenterY(View view) {
        float parentHeight = ((View) view.getParent()).getHeight();
        float centerY = parentHeight / 2;
        float viewY = (view.getY() + view.getHeight()) / 2;
        return Math.abs(centerY - viewY) <= 20;
    }

    private void setOnMultiTouchListener(OnMultiTouchListener onMultiTouchListener) {
        this.onMultiTouchListener = onMultiTouchListener;
    }

    public void setOnGestureControl(OnGestureControl onGestureControl) {
        this.onGestureControl = onGestureControl;
    }

    private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            if (onGestureControl != null) {
                onGestureControl.onClick();
            }
            return true;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            if (onGestureControl != null) {
                onGestureControl.onDown();
            }
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            super.onLongPress(e);
            if (onGestureControl != null) {
                onGestureControl.onLongClick();
            }
        }
    }
}
