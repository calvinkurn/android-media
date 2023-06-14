package com.tokopedia.play.widget.ui.carousel

import android.content.Context
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.play.widget.R
import kotlin.math.abs
import kotlin.math.min
import com.tokopedia.unifyprinciples.R as unifyR

/**
 * Created by kenny.hadisaputra on 04/05/23
 */
class PlayWidgetCarouselItemDecoration(
    context: Context
) : RecyclerView.ItemDecoration() {

    private val offset6 = context.resources.getDimensionPixelOffset(R.dimen.play_widget_dp_6)
    private val offset12 = context.resources.getDimensionPixelOffset(R.dimen.play_widget_dp_12)

    private val distanceBeforeScale = context.resources.getDimension(
        R.dimen.play_widget_carousel_distance_before_scale
    )
    private val maxShrink = 0.96f
    private val maxAlphaOverlay = 0.5f

    private val whiteColor = MethodChecker.getColor(context, unifyR.color.Unify_Static_White)

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

        val rect = Rect()
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = whiteColor
        }

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
                offset12.toFloat(),
                offset12.toFloat(),
                paint.apply {
                    alpha = ((1f - alphaOverlay) * 255).toInt()
                }
            )
        }
    }
}
