package com.tokopedia.content.common.ui.itemdecoration

import android.content.Context
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import kotlin.math.abs
import kotlin.math.min
import com.tokopedia.unifyprinciples.R as unifyprinciplesR
import com.tokopedia.content.common.R as contentcommonR

/**
 * Created By : Jonathan Darwin on July 24, 2023
 */
abstract class FocusedCarouselItemDecoration(
    context: Context
) : RecyclerView.ItemDecoration() {

    protected val offset6 = context.resources.getDimensionPixelOffset(contentcommonR.dimen.content_common_space_6)
    protected open val roundedOffset = context.resources.getDimensionPixelOffset(contentcommonR.dimen.content_common_space_12)

    private val distanceBeforeScale = context.resources.getDimension(
        contentcommonR.dimen.content_common_dp_200
    )
    protected open val maxShrink = 0.96f
    private val maxAlphaOverlay = 0.5f

    protected open val overlayColor = MethodChecker.getColor(context, unifyprinciplesR.color.Unify_Static_White)

    private val rect = Rect()
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = overlayColor
    }

    fun getHorizontalOffset(): Int {
        return 2 * offset6
    }
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.left = offset6
        outRect.right = offset6
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)

        val midPoint = parent.width / 2
        val itemCount = parent.childCount

        for (i in 0 until itemCount) {
            val child = parent.getChildAt(i)
            val childMiddleLocation = (2 * child.x + child.width) / 2
            val distanceToMidpoint = abs(childMiddleLocation - midPoint)

            val scale = 1f - min(distanceToMidpoint, distanceBeforeScale) / distanceBeforeScale * (1f - maxShrink)
            val alphaOverlay = 1f - min(distanceToMidpoint, distanceBeforeScale) / distanceBeforeScale * (1f - maxAlphaOverlay)
            child.scaleX = scale
            child.scaleY = scale

            child.getDrawingRect(rect)
            parent.offsetDescendantRectToMyCoords(child, rect)

            val scaleMatrix = Matrix().apply {
                setScale(scale, scale, rect.centerX().toFloat(), rect.centerY().toFloat())
            }

            val finalRect = RectF(rect)
            scaleMatrix.mapRect(finalRect)

            c.drawRoundRect(
                finalRect,
                roundedOffset.toFloat(),
                roundedOffset.toFloat(),
                paint.apply {
                    alpha = ((1f - alphaOverlay) * 255).toInt()
                }
            )
        }
    }
}

