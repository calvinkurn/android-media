package com.tokopedia.editor.ui.gesture.impl

import android.view.View
import com.tokopedia.editor.ui.gesture.api.ScaleGestureDetector
import com.tokopedia.editor.ui.gesture.listener.OnScaleGestureListener
import com.tokopedia.editor.ui.gesture.util.Vector2D
import com.tokopedia.editor.ui.gesture.util.VectorAngle
import com.tokopedia.editor.ui.model.AddTextModel

internal class ScaleGestureListener constructor(
    private val multiGestureListener: MultiGestureListener
) : OnScaleGestureListener {

    private var pivotX = 0f
    private var pivotY = 0f

    private var prevSpanVector = Vector2D()

    override fun onScaleBegin(view: View, detector: ScaleGestureDetector): Boolean {
        pivotX = detector.focusX
        pivotY = detector.focusY
        prevSpanVector.set(detector.currentSpanVector)
        return true
    }

    override fun onScale(view: View, detector: ScaleGestureDetector): Boolean {
        if (pivotX == 0f && pivotY == 0f) return false

        val info = AddTextModel(
            deltaX = detector.focusX - pivotX,
            deltaY = detector.focusY - pivotY,
            deltaScale = detector.getScaleFactor(),
            deltaAngle = VectorAngle.get(prevSpanVector, detector.currentSpanVector),
            pivotX = pivotX,
            pivotY = pivotY,
            minScale = MIN_SCALE,
            maxScale = MAX_SCALE
        )

        multiGestureListener.move(view, info)
        return false
    }

    override fun onScaleEnd(view: View, detector: ScaleGestureDetector) {}

    companion object {
        private const val MIN_SCALE = 0.2f
        private const val MAX_SCALE = 10.0f
    }
}
