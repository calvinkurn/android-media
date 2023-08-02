package com.tokopedia.editor.ui.gesture

import android.view.View
import com.tokopedia.editor.ui.gesture.listener.OnScaleGestureListener
import com.tokopedia.editor.ui.gesture.util.Vector2D
import com.tokopedia.editor.ui.gesture.util.VectorAngle
import com.tokopedia.editor.ui.model.AddTextModel
import com.tokopedia.editor.ui.gesture.java.ScaleGestureDetector

class ScaleGestureListener constructor(
    private val listener: MultiTouchListener
) : OnScaleGestureListener {

    private var pivotX: Float = 0f
    private var pivotY: Float = 0f

    private var prevSpanVector: Vector2D = Vector2D()

    override fun onScaleBegin(view: View, detector: ScaleGestureDetector): Boolean {
        pivotX = detector.focusX
        pivotY = detector.focusY
        prevSpanVector.set(detector.currentSpanVector)
        return listener.data.isTextPinchZoomable
    }

    override fun onScale(view: View, detector: ScaleGestureDetector): Boolean {
        val deltaX = if (listener.data.isTranslateEnabled) detector.focusX - pivotX else 0f
        val deltaY = if (listener.data.isTranslateEnabled) detector.focusY - pivotY else 0f
        val deltaScale = if (listener.data.isScaleEnabled) detector.scaleFactor else 1f
        val deltaAngle = if (listener.data.isRotateEnabled) VectorAngle.get(prevSpanVector, detector.currentSpanVector) else 0f

        val model = AddTextModel(
            deltaX = deltaX,
            deltaY = deltaY,
            deltaScale = deltaScale,
            deltaAngle = deltaAngle,
            pivotX = pivotX,
            pivotY = pivotY,
            minScale = listener.data.minimumScale,
            maxScale = listener.data.maximumScale
        )

        listener.move(view, model)
        return !listener.data.isTextPinchZoomable
    }

    override fun onScaleEnd(view: View, detector: ScaleGestureDetector) {}
}
