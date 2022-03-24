package com.tokopedia.video_widget.util

import android.view.View
import androidx.recyclerview.widget.RecyclerView

typealias ViewMeasurement = Pair<Int, Int>

sealed interface VisibilityMeasurementMethod {
    object VerticalOnly : VisibilityMeasurementMethod
    object HorizontalOnly : VisibilityMeasurementMethod
    object Area : VisibilityMeasurementMethod
}

internal object RecyclerViewUtils {
    fun getRecyclerViewLocationAndMeasurement(
        recyclerView: RecyclerView?
    ): Pair<IntArray, ViewMeasurement> {
        val recyclerViewPosition = intArrayOf(0, 0)
        recyclerView?.getLocationOnScreen(recyclerViewPosition)
        val recyclerViewHeight = recyclerView?.height ?: 0
        val recyclerViewWidth = recyclerView?.width ?: 0
        val measurement = ViewMeasurement(recyclerViewWidth, recyclerViewHeight)
        return Pair(recyclerViewPosition, measurement)
    }

    fun getViewVisibilityOnRecyclerView(
        view: View,
        recyclerViewLocation: IntArray, //location of recycler view on screen
        recyclerViewMeasurement: ViewMeasurement, // measurement of recycler view,
        measurementMethod: VisibilityMeasurementMethod = VisibilityMeasurementMethod.VerticalOnly,
    ): Float {
        val viewLocation = IntArray(2)
        view.getLocationOnScreen(viewLocation)
        val viewMeasurement = ViewMeasurement(view.width, view.height)
        return getViewVisibilityOnRecyclerView(
            recyclerViewLocation,
            recyclerViewMeasurement,
            viewLocation,
            viewMeasurement,
            measurementMethod
        )
    }

    private fun getViewVisibilityOnRecyclerView(
        recyclerViewLocation: IntArray, //location of recycler view on screen
        recyclerViewMeasurement: ViewMeasurement, // measurement of recycler view
        viewLocation: IntArray, // location of view on screen, you can use the method of view class's getLocationOnScreen method.
        viewMeasurement: ViewMeasurement,
        measurementMethod: VisibilityMeasurementMethod,
    ): Float {
        return when (measurementMethod) {
            VisibilityMeasurementMethod.Area -> getAreaVisibilityPercentage(
                recyclerViewLocation,
                recyclerViewMeasurement,
                viewLocation,
                viewMeasurement
            )
            VisibilityMeasurementMethod.HorizontalOnly -> getHorizontalVisibilityPercentage(
                recyclerViewLocation,
                recyclerViewMeasurement,
                viewLocation,
                viewMeasurement
            )
            VisibilityMeasurementMethod.VerticalOnly -> getVerticalVisibilityPercentage(
                recyclerViewLocation,
                recyclerViewMeasurement,
                viewLocation,
                viewMeasurement
            )
        }
    }

    private fun getVerticalVisibilityPercentage(
        recyclerViewLocation: IntArray, //location of recycler view on screen
        recyclerViewMeasurement: ViewMeasurement, // measurement of recycler view
        viewLocation: IntArray, // location of view on screen, you can use the method of view class's getLocationOnScreen method.
        viewMeasurement: ViewMeasurement,
    ): Float {
        val (_, recyclerViewHeight) = recyclerViewMeasurement
        val (_, viewHeight) = viewMeasurement

        val recyclerViewTop = recyclerViewLocation[1]
        val recyclerViewBottom = recyclerViewHeight + recyclerViewTop

        val viewTop = viewLocation[1]
        val viewBottom = viewHeight + viewTop //Get the bottom of view.

        var visiblePercent: Float = 0f
        if (viewTop >= recyclerViewTop) {  //if view's top is inside the recycler view.
            visiblePercent = 100f
            //Get the bottom of recycler view
            if (viewBottom >= recyclerViewBottom) {   //If view's bottom is outside from recycler view
                //Find the visible part of view by subtracting view's top from recyclerview's bottom
                val visiblePart = recyclerViewBottom - viewTop
                visiblePercent = visiblePart.toFloat() / viewHeight * 100
            }
        } else {      //if view's top is outside the recycler view.
            if (viewBottom > recyclerViewTop) { //if view's bottom is outside the recycler view
                //Find the visible part of view by subtracting recycler view's top from view's bottom
                val visiblePart = viewBottom - recyclerViewTop
                visiblePercent = visiblePart.toFloat() / viewHeight * 100
            }
        }
        return visiblePercent
    }

    private fun getHorizontalVisibilityPercentage(
        recyclerViewLocation: IntArray, //location of recycler view on screen
        recyclerViewMeasurement: ViewMeasurement, // measurement of recycler view
        viewLocation: IntArray, // location of view on screen, you can use the method of view class's getLocationOnScreen method.
        viewMeasurement: ViewMeasurement,
    ): Float {
        val (recyclerViewWidth, _) = recyclerViewMeasurement
        val (viewWidth, _) = viewMeasurement

        val recyclerViewLeft = recyclerViewLocation[0]
        val recyclerViewRight = recyclerViewLeft + recyclerViewWidth

        val viewLeft = viewLocation[0]
        val viewRight = viewWidth + viewLeft

        var visiblePercent: Float = 0f
        if (viewLeft >= recyclerViewLeft) {  //if view's left is inside the recycler view.
            visiblePercent = 100f
            //Get the right of recycler view
            if (viewRight >= recyclerViewRight) {   //If view's right is outside from recycler view
                //Find the visible part of view by subtracting view's left from recyclerview's right
                val visiblePart = recyclerViewRight - viewLeft
                visiblePercent = visiblePart.toFloat() / viewWidth * 100
            }
        } else {      //if view's right is outside the recycler view.
            if (viewRight > recyclerViewLeft) { //if view's right is outside the recycler view
                //Find the visible part of view by subtracting recycler view's left from view's right
                val visiblePart = viewRight - recyclerViewLeft
                visiblePercent = visiblePart.toFloat() / viewWidth * 100
            }
        }
        return visiblePercent
    }

    private fun getAreaVisibilityPercentage(
        recyclerViewLocation: IntArray, //location of recycler view on screen
        recyclerViewMeasurement: ViewMeasurement, // measurement of recycler view
        viewLocation: IntArray, // location of view on screen, you can use the method of view class's getLocationOnScreen method.
        viewMeasurement: ViewMeasurement,
    ): Float {
        val (recyclerViewWidth, recyclerViewHeight) = recyclerViewMeasurement
        val (viewWidth, viewHeight) = viewMeasurement
        val viewArea = viewHeight * viewWidth

        val recyclerViewLeft = recyclerViewLocation[0]
        val recyclerViewRight = recyclerViewLeft + recyclerViewWidth
        val recyclerViewTop = recyclerViewLocation[1]
        val recyclerViewBottom = recyclerViewHeight + recyclerViewTop

        val viewLeft = viewLocation[0]
        val viewRight = viewWidth + viewLeft
        val viewTop = viewLocation[1]
        val viewBottom = viewHeight + viewTop //Get the bottom of view.

        val visibleHeight =
            getVisibleHeight(viewTop, viewBottom, recyclerViewTop, recyclerViewBottom)
        val visibleWidth = getVisibleWidth(viewLeft, viewRight, recyclerViewLeft, recyclerViewRight)
        val visibleArea = visibleHeight * visibleWidth

        return visibleArea.toFloat() / viewArea * 100
    }

    private fun getVisibleHeight(
        viewTop: Int,
        viewBottom: Int,
        recyclerViewTop: Int,
        recyclerViewBottom: Int,
    ) : Int {
        var visibleHeight = viewBottom - viewTop
        if (viewTop >= recyclerViewTop) {
            if (viewBottom >= recyclerViewBottom) {
                visibleHeight = recyclerViewBottom - viewTop
            }
        } else {
            if (viewBottom > recyclerViewTop) {
                visibleHeight = viewBottom - recyclerViewTop
            }
        }
        return visibleHeight
    }

    private fun getVisibleWidth(
        viewLeft: Int,
        viewRight: Int,
        recyclerViewLeft: Int,
        recyclerViewRight: Int,
    ) : Int {
        var visibleWidth = viewRight - viewLeft
        if (viewLeft >= recyclerViewLeft) {
            if (viewRight >= recyclerViewRight) {
                visibleWidth = recyclerViewRight - viewLeft
            }
        } else {
            if (viewRight > recyclerViewLeft) {
                visibleWidth = viewRight - recyclerViewLeft
            }
        }
        return visibleWidth
    }
}