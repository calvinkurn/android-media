package com.tokopedia.editor.ui.gesture.impl

import android.view.View
import com.tokopedia.editor.ui.gesture.api.ScaleGestureDetector
import com.tokopedia.editor.ui.gesture.listener.OnScaleGestureListener
import com.tokopedia.editor.ui.gesture.util.Vector2D
import com.tokopedia.editor.ui.gesture.util.VectorAngle
import com.tokopedia.editor.ui.model.AddTextModel

class SetScaleGestureListener constructor(
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
        val info = AddTextModel(
            detector.focusX - pivotX,
            detector.focusY - pivotY,
            detector.scaleFactor,
            VectorAngle.get(prevSpanVector, detector.currentSpanVector),
            pivotX,
            pivotY,
            0.2f,
            10f
        )
        multiGestureListener.move(view, info)
        return false
    }

    override fun onScaleEnd(view: View, detector: ScaleGestureDetector) {}
}
