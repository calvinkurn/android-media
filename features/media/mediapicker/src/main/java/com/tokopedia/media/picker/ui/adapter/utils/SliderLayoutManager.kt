package com.tokopedia.media.picker.ui.adapter.utils

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs
import kotlin.math.sqrt

class SliderLayoutManager(context: Context?) : LinearLayoutManager(context) {

    private lateinit var recyclerView: RecyclerView

    init {
        orientation = HORIZONTAL
    }

    override fun onAttachedToWindow(view: RecyclerView) {
        super.onAttachedToWindow(view)
        recyclerView = view

        if (recyclerView.onFlingListener == null) {
            LinearSnapHelper().attachToRecyclerView(recyclerView)
        }
    }

    override fun onScrollStateChanged(state: Int) {
        super.onScrollStateChanged(state)

        // When scroll stops we notify on the selected item
        if (state == RecyclerView.SCROLL_STATE_IDLE) {

            // Find the closest child to the recyclerView center --> this is the selected item.
            val recyclerViewCenterX = recyclerViewCenterX()
            var minDistance = recyclerView.width
            var position = -1

            for (i in 0 until recyclerView.childCount) {
                val child = recyclerView.getChildAt(i)
                val childCenterX = getDecoratedLeft(child) + (getDecoratedRight(child) - getDecoratedLeft(child)) / 2
                val newDistance = abs(childCenterX - recyclerViewCenterX)

                if (newDistance < minDistance) {
                    minDistance = newDistance
                    position = recyclerView.getChildLayoutPosition(child)
                }
            }
        }
    }

    private fun scaleDownView() {
        val mid = width / 2.0f

        for (i in 0 until childCount) {

            // Calculating the distance of the child from the center
            val child = getChildAt(i)?: return

            val childMid = (getDecoratedLeft(child) + getDecoratedRight(child)) / 2.0f
            val distanceFromCenter = abs(mid - childMid)

            // The scaling formula
            val scale = 1 - sqrt((distanceFromCenter / width).toDouble()).toFloat() * 0.66f

            // Set scale to view
            child.scaleX = scale
            child.scaleY = scale
        }
    }

    private fun recyclerViewCenterX() : Int {
        return (recyclerView.right - recyclerView.left) / 2 + recyclerView.left
    }

}
