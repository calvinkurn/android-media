package com.tokopedia.video_widget.util

import android.view.View
import androidx.recyclerview.widget.RecyclerView

internal object RecyclerViewUtils {
    fun getRecyclerViewLocationAndHeight(recyclerView: RecyclerView?): Pair<IntArray, Int> {
        val recyclerViewPosition = intArrayOf(0, 0)
        recyclerView?.getLocationOnScreen(recyclerViewPosition)
        val recyclerViewHeight = recyclerView?.height ?: 0
        return Pair(recyclerViewPosition, recyclerViewHeight)
    }

    fun getViewVisibilityOnRecyclerView(
        view: View,
        recyclerViewLocation: IntArray, //location of recycler view on screen
        recyclerViewHeight: Int, // height of recycler view
    ): Float {
        val viewLocation = IntArray(2)
        view.getLocationOnScreen(viewLocation)
        val viewHeight = view.height
        return getViewVisibilityOnRecyclerView(
            recyclerViewLocation,
            recyclerViewHeight,
            viewLocation,
            viewHeight
        )
    }

    internal fun getViewVisibilityOnRecyclerView(
        recyclerViewLocation: IntArray, //location of recycler view on screen
        recyclerViewHeight: Int, // height of recycler view
        viewLocation: IntArray, // location of view on screen, you can use the method of view class's getLocationOnScreen method.
        viewHeight: Int //  height of view
    ): Float {
        var visiblePercent: Float = 0f
        val viewBottom = viewHeight + viewLocation[1] //Get the bottom of view.
        if (viewLocation[1] >= recyclerViewLocation[1]) {  //if view's top is inside the recycler view.
            visiblePercent = 100f
            //Get the bottom of recycler view
            val recyclerViewBottom = recyclerViewHeight + recyclerViewLocation[1]
            if (viewBottom >= recyclerViewBottom) {   //If view's bottom is outside from recycler view
                //Find the visible part of view by subtracting view's top from recyclerview's bottom
                val visiblePart = recyclerViewBottom - viewLocation[1]
                visiblePercent = visiblePart.toFloat() / viewHeight * 100
            }
        } else {      //if view's top is outside the recycler view.
            if (viewBottom > recyclerViewLocation[1]) { //if view's bottom is outside the recycler view
                //Find the visible part of view by subtracting recycler view's top from view's bottom
                val visiblePart = viewBottom - recyclerViewLocation[1]
                visiblePercent = visiblePart.toFloat() / viewHeight * 100
            }
        }
        return visiblePercent
    }
}