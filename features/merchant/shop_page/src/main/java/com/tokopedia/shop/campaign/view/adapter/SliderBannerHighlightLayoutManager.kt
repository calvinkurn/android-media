package com.tokopedia.shop.campaign.view.adapter

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import kotlin.math.abs
import kotlin.math.sqrt

class SliderBannerHighlightLayoutManager(
    context: Context
) : LinearLayoutManager(context, HORIZONTAL, false) {

    companion object {
        private const val INT_TWO = 2
        private const val SCALING_CONST = 0.2f
    }

    private var recyclerView: RecyclerView? = null

    override fun onAttachedToWindow(view: RecyclerView) {
        super.onAttachedToWindow(view)
        recyclerView = view
        if (recyclerView?.onFlingListener == null) {
            LinearSnapHelper().attachToRecyclerView(recyclerView)
        }
    }

    override fun onLayoutCompleted(state: RecyclerView.State?) {
        super.onLayoutCompleted(state)
        scaleChild()
    }

    override fun scrollHorizontallyBy(
        dx: Int,
        recycler: RecyclerView.Recycler?,
        state: RecyclerView.State?
    ): Int {
        return if (orientation == HORIZONTAL) {
            super.scrollHorizontallyBy(dx, recycler, state).also {
                scaleChild()
            }
        } else {
            Int.ZERO
        }
    }

    private fun scaleChild() {
        val mid = width / INT_TWO.toFloat()
        for (i in Int.ZERO until childCount) {
            // Calculating the distance of the child from the center
            val child = getChildAt(i) ?: return
            val childMid = (getDecoratedLeft(child) + getDecoratedRight(child)) / INT_TWO.toFloat()
            val distanceFromCenter = abs(mid - childMid)
            // The scaling formula, will scale view on the left and right of middle child
            val scale =
                Int.ONE - sqrt((distanceFromCenter / width).toDouble()).toFloat() * SCALING_CONST
            // Set scale to child
            child.scaleX = scale
            child.scaleY = scale
        }
    }
}
