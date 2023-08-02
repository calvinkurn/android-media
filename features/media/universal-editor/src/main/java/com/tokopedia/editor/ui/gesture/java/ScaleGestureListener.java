package com.tokopedia.editor.ui.gesture.java;

import android.view.View;

import com.tokopedia.editor.ui.gesture.listener.OnScaleGestureListener;
import com.tokopedia.editor.ui.gesture.util.Vector2D;
import com.tokopedia.editor.ui.gesture.util.VectorAngle;
import com.tokopedia.editor.ui.model.AddTextModel;

public class ScaleGestureListener implements OnScaleGestureListener {

    private MultiTouchListener multiTouchListener;

    ScaleGestureListener(MultiTouchListener multiTouchListener) {
        this.multiTouchListener = multiTouchListener;
    }

    private float pivotX;
    private float pivotY;
    private Vector2D prevSpanVector = new Vector2D();

    @Override
    public boolean onScaleBegin(View view, ScaleGestureDetector detector) {
        pivotX = detector.getFocusX();
        pivotY = detector.getFocusY();
        prevSpanVector.set(detector.getCurrentSpanVector());
        return multiTouchListener.isTextPinchZoomable;
    }

    @Override
    public void onScaleEnd(View view, ScaleGestureDetector detector) {

    }

    @Override
    public boolean onScale(View view, ScaleGestureDetector detector) {
        AddTextModel info = new AddTextModel(
                multiTouchListener.isTranslateEnabled ? detector.getFocusX() - pivotX : 0.0f,
                multiTouchListener.isTranslateEnabled ? detector.getFocusY() - pivotY : 0.0f,
                multiTouchListener.isScaleEnabled ? detector.getScaleFactor() : 1.0f,
                multiTouchListener.isRotateEnabled ? VectorAngle.get(prevSpanVector, detector.getCurrentSpanVector()) : 0.0f,
                pivotX,
                pivotY,
                multiTouchListener.minimumScale,
                multiTouchListener.maximumScale
        );
        multiTouchListener.move(view, info);
        return !multiTouchListener.isTextPinchZoomable;
    }
}
