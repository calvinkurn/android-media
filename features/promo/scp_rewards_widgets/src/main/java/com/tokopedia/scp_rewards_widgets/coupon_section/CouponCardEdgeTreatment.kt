package com.tokopedia.scp_rewards_widgets.coupon_section

import android.content.Context
import androidx.annotation.FloatRange
import androidx.annotation.RestrictTo
import com.google.android.material.shape.EdgeTreatment
import com.google.android.material.shape.ShapePath
import com.tokopedia.scp_rewards_common.utils.dpToPx

/**
 * The code for CouponCardEdgeTreatment is adapted from BottomAppBarTopEdgeTreatment which is a part
 * of Material Design. BottomAppBarTopEdgeTreatment is used to draw a semicircle cradle for the
 * Floating Action Button (FAB). It has been modified so that it can be used to draw scallops
 * (semicircle notch) on left and right edge of CardView so that it appears like a Ticket Voucher.
 *
 *
 * 2 major difference between CouponCardEdgeTreatment and BottomAppBarTopEdgeTreatment
 * - In BottomAppBarTopEdgeTreatment the position of the notch was set using the center parameter
 * from getEdgePath(). In VoucherCardEdgeTreatment the dependency on center has been removed and
 * now horizontalOffset is used to set the position of the notch.
 * - In CouponCardEdgeTreatment, isLeftEdge flag has been introduced. This flag is used to find
 * the correct notch position depending on whether the edge is left or right.
 */
class CouponCardEdgeTreatment(private val context: Context,
                              private val scallopMargin: Float = 0f,
                              private val roundedCornerRadius: Float = 0f,
                              cradleVerticalOffset: Float = 0f,
                              horizontalOffset: Float = 0f,
                              isLeftEdge: Boolean = false) : EdgeTreatment(), Cloneable {
    /**
     * Returns current scallop diameter in pixels.
     *
     * @hide
     */
    /**
     * Sets the scallop diameter the size of the scallop in pixels.
     *
     * @hide
     */
    @get:RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    @set:RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    var scallopDiameter = 0f
    private var cradleVerticalOffset = 0f
    private val horizontalOffset: Float
    private val isLeftEdge: Boolean

    init {
        setCradleVerticalOffset(cradleVerticalOffset)
        this.horizontalOffset = horizontalOffset
        this.isLeftEdge = isLeftEdge
    }

    override fun getEdgePath(
        length: Float, center: Float, interpolation: Float, shapePath: ShapePath) {
        if (scallopDiameter == 0f) {
            // There is no cutout to draw.
            shapePath.lineTo(length, 0f)
            return
        }
        val cradleDiameter = scallopMargin * 2 + scallopDiameter
        val cradleRadius = cradleDiameter / 2f
        val roundedCornerOffset = interpolation * roundedCornerRadius

        // The right edge is drawn from top to bottom. So, the cradlePosition can be set by passing
        // the distance from the top to cradle position in horizontalOffset parameter.
        // The left edge is drawn from bottom to top. Here, horizontalOffset refers to the distance
        // from the bottom to the cradle position. As the card is expanding, the horizontalOffset
        // is added with the increased length so that the cradle appears to stay in the same position.
        val cradlePosition: Float = if (isLeftEdge) {
            horizontalOffset + (length - dpToPx(context = context, DEFAULT_CARD_HEIGHT))
        } else {
            horizontalOffset
        }

        // The center offset of the cutout tweens between the vertical offset when attached, and the
        // cradleRadius as it becomes detached.
        val verticalOffset = interpolation * cradleVerticalOffset + (1 - interpolation) * cradleRadius
        val verticalOffsetRatio = verticalOffset / cradleRadius
        if (verticalOffsetRatio >= 1.0f) {
            // Vertical offset is so high that there's no curve to draw in the edge, i.e., the fab is
            // actually above the edge so just draw a straight line.
            shapePath.lineTo(length, 0f)
            return  // Early exit.
        }

        // Calculate the path of the cutout by calculating the location of two adjacent circles. One
        // circle is for the rounded corner. If the rounded corner circle radius is 0 the corner will
        // not be rounded. The other circle is the cutout.

        // Calculate the X distance between the center of the two adjacent circles using pythagorean
        // theorem.
        val distanceBetweenCenters = cradleRadius + roundedCornerOffset
        val distanceBetweenCentersSquared = distanceBetweenCenters * distanceBetweenCenters
        val distanceY = verticalOffset + roundedCornerOffset
        val distanceX = Math.sqrt((distanceBetweenCentersSquared - distanceY * distanceY).toDouble()).toFloat()

        // Calculate the x position of the rounded corner circles.
        val leftRoundedCornerCircleX = cradlePosition - distanceX
        val rightRoundedCornerCircleX = cradlePosition + distanceX

        // Calculate the arc between the center of the two circles.
        val cornerRadiusArcLength = Math.toDegrees(Math.atan((distanceX / distanceY).toDouble())).toFloat()
        val cutoutArcOffset = ARC_QUARTER - cornerRadiusArcLength

        // Draw the starting line up to the left rounded corner.
        shapePath.lineTo(
            /* x= */leftRoundedCornerCircleX,
            /* y= */0f)

        // Draw the arc for the left rounded corner circle. The bounding box is the area around the
        // circle's center which is at `(leftRoundedCornerCircleX, roundedCornerOffset)`.
        shapePath.addArc(
            /* left= */leftRoundedCornerCircleX - roundedCornerOffset,
            /* top= */0f,
            /* right= */leftRoundedCornerCircleX + roundedCornerOffset,
            /* bottom= */roundedCornerOffset * 2,
            /* startAngle= */ANGLE_UP.toFloat(),
            /* sweepAngle= */cornerRadiusArcLength)

        // Draw the cutout circle.
        shapePath.addArc(
            /* left= */cradlePosition - cradleRadius,
            /* top= */-cradleRadius - verticalOffset,
            /* right= */cradlePosition + cradleRadius,
            /* bottom= */cradleRadius - verticalOffset,
            /* startAngle= */ANGLE_LEFT - cutoutArcOffset,
            /* sweepAngle= */cutoutArcOffset * 2 - ARC_HALF)

        // Draw an arc for the right rounded corner circle. The bounding box is the area around the
        // circle's center which is at `(rightRoundedCornerCircleX, roundedCornerOffset)`.
        shapePath.addArc(
            /* left= */rightRoundedCornerCircleX - roundedCornerOffset,
            /* top= */0f,
            /* right= */rightRoundedCornerCircleX + roundedCornerOffset,
            /* bottom= */roundedCornerOffset * 2,
            /* startAngle= */ANGLE_UP - cornerRadiusArcLength,
            /* sweepAngle= */cornerRadiusArcLength)

        // Draw the ending line after the right rounded corner.
        shapePath.lineTo(
            /* x= */length,
            /* y= */0f)
    }

    /**
     * Sets the vertical offset, in pixel. An offset of 0 indicates the vertical center of the
     * scallop is positioned on the top edge.
     */
    fun setCradleVerticalOffset(@FloatRange(from = 0.0) cradleVerticalOffset: Float) {
        require(!(cradleVerticalOffset < 0)) { "cradleVerticalOffset must be positive." }
        this.cradleVerticalOffset = cradleVerticalOffset
    }

    companion object {
        private const val ARC_QUARTER = 90
        private const val ARC_HALF = 180
        private const val ANGLE_UP = 270
        private const val ANGLE_LEFT = 180
        private const val DEFAULT_CARD_HEIGHT = 128
    }
}
