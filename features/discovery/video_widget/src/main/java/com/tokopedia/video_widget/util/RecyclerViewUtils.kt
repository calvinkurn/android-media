package com.tokopedia.video_widget.util

import android.view.View
import androidx.recyclerview.widget.RecyclerView

internal data class ViewMeasurement(
    val width: Int,
    val height: Int
)

internal sealed interface VisibilityMeasurementMethod {
    object Vertical : VisibilityMeasurementMethod
    object Horizontal : VisibilityMeasurementMethod
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
        measurementMethod: VisibilityMeasurementMethod = VisibilityMeasurementMethod.Vertical,
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
            VisibilityMeasurementMethod.Horizontal -> getHorizontalVisibilityPercentage(
                recyclerViewLocation,
                recyclerViewMeasurement,
                viewLocation,
                viewMeasurement
            )
            VisibilityMeasurementMethod.Vertical -> getVerticalVisibilityPercentage(
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

        return getVisiblePartPercentage(
            viewTop,
            viewBottom,
            viewHeight,
            recyclerViewTop,
            recyclerViewBottom
        )
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

        return getVisiblePartPercentage(
            viewLeft,
            viewRight,
            viewWidth,
            recyclerViewLeft,
            recyclerViewRight
        )
    }

    private fun getVisiblePartPercentage(
        viewStart: Int,
        viewEnd: Int,
        viewTotalPart: Int,
        recyclerViewStart: Int,
        recyclerViewEnd: Int,
    ): Float {
        var visiblePercent = 0f
        if (viewStart >= recyclerViewStart) {
            visiblePercent = 100f
            if (viewEnd >= recyclerViewEnd) {
                val visiblePart = recyclerViewEnd - viewStart
                visiblePercent = visiblePart.toFloat() / viewTotalPart * 100
            }
        } else {
            if (viewEnd > recyclerViewStart) {
                val visiblePart = viewEnd - recyclerViewStart
                visiblePercent = visiblePart.toFloat() / viewTotalPart * 100
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

        val visibleHeight = getVisiblePart(viewTop, viewBottom, recyclerViewTop, recyclerViewBottom)
        val visibleWidth = getVisiblePart(viewLeft, viewRight, recyclerViewLeft, recyclerViewRight)
        val visibleArea = visibleHeight * visibleWidth

        return visibleArea.toFloat() / viewArea * 100
    }

    private fun getVisiblePart(
        viewStart: Int,
        viewEnd: Int,
        recyclerViewStart: Int,
        recyclerViewEnd: Int,
    ): Int {
        var visiblePart = 0
        if (viewStart >= recyclerViewStart) {
            visiblePart = viewEnd - viewStart
            if (viewEnd >= recyclerViewEnd) {
                visiblePart = recyclerViewEnd - viewStart
            }
        } else {
            if (viewEnd > recyclerViewStart) {
                visiblePart = viewEnd - recyclerViewStart
            }
        }
        return visiblePart
    }
}