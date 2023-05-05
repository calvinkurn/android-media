package com.tokopedia.play.widget.ui.carousel

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.play.widget.R
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

/**
 * Created by kenny.hadisaputra on 04/05/23
 */
class PlayWidgetCarouselItemDecoration(
    context: Context,
) : RecyclerView.ItemDecoration() {

    private val offset12 = context.resources.getDimensionPixelOffset(R.dimen.play_widget_dp_12)

    private val distanceBeforeScale = context.resources.getDimension(
        R.dimen.play_widget_carousel_distance_before_scale
    )
    private val maxShrink = 0.96f

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.left = offset12
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)

        val midPoint = parent.width / 2
        val itemCount = parent.childCount
        for (i in 0 until itemCount) {
            val child = parent.getChildAt(i)
            val childMiddleLocation = (2 * child.x + child.width) / 2
            val distanceToMidpoint = abs(childMiddleLocation - midPoint)

            val scale = 1f - min(distanceToMidpoint, distanceBeforeScale) / distanceBeforeScale * (1f - maxShrink)
            child.scaleX = scale
            child.scaleY = scale
        }
    }
}
