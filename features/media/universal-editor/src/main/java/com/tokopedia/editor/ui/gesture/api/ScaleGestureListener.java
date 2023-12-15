package com.tokopedia.editor.ui.gesture.api;

import android.view.View;

import com.tokopedia.editor.ui.gesture.listener.OnScaleGestureListener;
import com.tokopedia.editor.ui.gesture.util.Vector2D;
import com.tokopedia.editor.ui.gesture.util.VectorAngle;
import com.tokopedia.editor.ui.model.AddTextModel;

public class ScaleGestureListener implements OnScaleGestureListener {

    private V1MultiTouchListener v1MultiTouchListener;

    ScaleGestureListener(V1MultiTouchListener v1MultiTouchListener) {
        this.v1MultiTouchListener = v1MultiTouchListener;
    }

    private float pivotX;
    private float pivotY;
    private Vector2D prevSpanVector = new Vector2D();

    @Override
    public boolean onScaleBegin(View view, ScaleGestureDetector detector) {
        pivotX = detector.getFocusX();
        pivotY = detector.getFocusY();
        prevSpanVector.set(detector.getCurrentSpanVector());
        return v1MultiTouchListener.isTextPinchZoomable;
    }

    @Override
    public void onScaleEnd(View view, ScaleGestureDetector detector) {

    }

    @Override
    public boolean onScale(View view, ScaleGestureDetector detector) {
        AddTextModel info = new AddTextModel(
                v1MultiTouchListener.isTranslateEnabled ? detector.getFocusX() - pivotX : 0.0f,
                v1MultiTouchListener.isTranslateEnabled ? detector.getFocusY() - pivotY : 0.0f,
                v1MultiTouchListener.isScaleEnabled ? detector.getScaleFactor() : 1.0f,
                v1MultiTouchListener.isRotateEnabled ? VectorAngle.get(prevSpanVector, detector.getCurrentSpanVector()) : 0.0f,
                pivotX,
                pivotY,
                v1MultiTouchListener.minimumScale,
                v1MultiTouchListener.maximumScale
        );
        v1MultiTouchListener.move(view, info);
        return !v1MultiTouchListener.isTextPinchZoomable;
    }
}
