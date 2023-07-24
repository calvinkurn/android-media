package com.tokopedia.carouselproductcard.helper

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView

class StartPagerSnapHelper(
    private val pagingPaddingHorizontal: Int,
    private val itemPerPage: Int,
): PagerSnapHelper() {

    private var mVerticalHelper: OrientationHelper? = null
    private var mHorizontalHelper: OrientationHelper? = null

    override fun calculateDistanceToFinalSnap(
        layoutManager: RecyclerView.LayoutManager,
        targetView: View
    ): IntArray {
        val out = IntArray(2)

        if (layoutManager.canScrollHorizontally())
            out[0] = distanceToStart(targetView, getHorizontalHelper(layoutManager))
        else
            out[0] = 0

        if (layoutManager.canScrollVertically())
            out[1] = distanceToStart(targetView, getVerticalHelper(layoutManager))
        else
            out[1] = 0

        return out
    }

    private fun distanceToStart(targetView: View, helper: OrientationHelper): Int =
        helper.getDecoratedStart(targetView) - helper.startAfterPadding - (pagingPaddingHorizontal / 2)

    private fun getVerticalHelper(layoutManager: RecyclerView.LayoutManager): OrientationHelper {
        if (mVerticalHelper?.layoutManager !== layoutManager)
            mVerticalHelper = OrientationHelper.createVerticalHelper(layoutManager)
        return mVerticalHelper!!
    }

    private fun getHorizontalHelper(
        layoutManager: RecyclerView.LayoutManager
    ): OrientationHelper {
        if (mHorizontalHelper?.layoutManager !== layoutManager)
            mHorizontalHelper = OrientationHelper.createHorizontalHelper(layoutManager)
        return mHorizontalHelper!!
    }

    override fun findSnapView(layoutManager: RecyclerView.LayoutManager): View? =
        if (layoutManager is LinearLayoutManager)
            if (layoutManager.canScrollHorizontally())
                getStartView(layoutManager, getHorizontalHelper(layoutManager))
            else
                getStartView(layoutManager, getVerticalHelper(layoutManager))
        else
            super.findSnapView(layoutManager)

    private fun getStartView(
        layoutManager: RecyclerView.LayoutManager,
        helper: OrientationHelper,
    ): View? {
        if (layoutManager !is LinearLayoutManager)
            return super.findSnapView(layoutManager)

        val firstChildPosition = layoutManager.findFirstVisibleItemPosition()
        val isLastItem = isLastItem(layoutManager)

        if (firstChildPosition == RecyclerView.NO_POSITION || isLastItem) return null

        val child = layoutManager.findViewByPosition(firstChildPosition)

        return if (
            helper.getDecoratedEnd(child) >= helper.getDecoratedMeasurement(child) / 2
            && helper.getDecoratedEnd(child) > 0
        ) {
            child
        } else {
            layoutManager.findViewByPosition(firstChildPosition + itemPerPage)
        }
    }

    private fun isLastItem(layoutManager: LinearLayoutManager) =
        layoutManager.findLastCompletelyVisibleItemPosition() == layoutManager.itemCount - 1
}
