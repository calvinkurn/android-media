package com.tokopedia.discovery2.viewcontrollers.customview

import com.google.android.material.shape.EdgeTreatment
import com.google.android.material.shape.ShapePath

class TriangleEdgeTreatment(
    private val size: Float,
    private val heightRatio: Int,
    private val inside: Boolean
) : EdgeTreatment() {

    override fun getEdgePath(
        length: Float,
        center: Float,
        interpolation: Float,
        shapePath: ShapePath
    ) {
        shapePath.lineTo(center - size * interpolation, 0f)

        val yPosition = (size * interpolation) / heightRatio
        shapePath.lineTo(center, if (inside) yPosition else -yPosition)

        shapePath.lineTo(center + size * interpolation, 0f)
        shapePath.lineTo(length, 0f)
    }
}
