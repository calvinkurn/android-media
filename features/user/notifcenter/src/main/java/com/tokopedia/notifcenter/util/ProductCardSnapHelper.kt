package com.tokopedia.notifcenter.util

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView

class ProductCardSnapHelper: LinearSnapHelper() {

    private lateinit var layoutManager: RecyclerView.LayoutManager

    private val horizontalHelper by lazy {
        OrientationHelper.createHorizontalHelper(layoutManager)
    }

    private val verticalHelper by lazy {
        OrientationHelper.createVerticalHelper(layoutManager)
    }

    override fun attachToRecyclerView(recyclerView: RecyclerView?) {
        recyclerView?.layoutManager?.let { layoutManager = it }
        super.attachToRecyclerView(recyclerView)
    }

    override fun calculateDistanceToFinalSnap(layoutManager: RecyclerView.LayoutManager, targetView: View): IntArray? {
        val outSize = IntArray(2)
        if (layoutManager.canScrollHorizontally()) {
            outSize[0] = distanceToStart(targetView, horizontalHelper)
        } else {
            outSize[0] = 0
        }

        if (layoutManager.canScrollVertically()) {
            outSize[1] = distanceToStart(targetView, verticalHelper)
        } else {
            outSize[0] = 0
        }

        return super.calculateDistanceToFinalSnap(layoutManager, targetView)
    }

    override fun findSnapView(layoutManager: RecyclerView.LayoutManager?): View? {
        if (layoutManager is LinearLayoutManager) {
            return if (layoutManager.canScrollHorizontally()) {
                startView(layoutManager, horizontalHelper)
            } else {
                startView(layoutManager, verticalHelper)
            }
        }
        return super.findSnapView(layoutManager)
    }

    private fun startView(layoutManager: RecyclerView.LayoutManager, helper: OrientationHelper): View? {
        if (layoutManager is LinearLayoutManager) {
            val firstItem = layoutManager.findFirstVisibleItemPosition()
            val isLastItem = layoutManager.findLastCompletelyVisibleItemPosition() == layoutManager.itemCount - 1
            if (firstItem == RecyclerView.NO_POSITION || isLastItem) {
                return null
            }

            val childView = layoutManager.findViewByPosition(firstItem)
            if (helper.getDecoratedEnd(childView) >= helper.getDecoratedMeasurement(childView) / 2 &&
                helper.getDecoratedEnd(childView) > 0) {
                return childView
            } else {
                if (layoutManager.findLastCompletelyVisibleItemPosition() == layoutManager.itemCount - 1) {
                    return null
                } else {
                    return layoutManager.findViewByPosition(firstItem + 1)
                }
            }
        }
        return super.findSnapView(layoutManager)
    }

    private fun distanceToStart(targetView: View, helper: OrientationHelper): Int {
        return helper.getDecoratedStart(targetView) - helper.startAfterPadding
    }

}