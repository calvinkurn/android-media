package com.tokopedia.scp_rewards_widgets.coupon_section

import com.google.android.material.shape.CornerTreatment
import com.google.android.material.shape.ShapePath


class OutsideCircleCornerTreatment(private val radius: Float) : CornerTreatment() {
    companion object {
        private const val ANGLE_LEFT = 180f
    }

    override fun getCornerPath(
        shapePath: ShapePath,
        angle: Float,
        interpolation: Float,
        radius: Float
    ) {
        val interpolatedRadius = this.radius * interpolation
        shapePath.reset(0f, interpolatedRadius, ANGLE_LEFT, ANGLE_LEFT - angle)
        shapePath.addArc(0f, -interpolatedRadius, 2 * interpolatedRadius, interpolatedRadius, ANGLE_LEFT, angle)
        shapePath.lineTo(interpolatedRadius, 0f)
        shapePath.lineTo(0f, 0f)
    }
}
