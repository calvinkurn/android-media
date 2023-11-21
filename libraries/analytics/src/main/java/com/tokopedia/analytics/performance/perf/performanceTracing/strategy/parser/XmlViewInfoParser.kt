package com.tokopedia.analytics.performance.perf.performanceTracing.strategy.parser

import android.graphics.Rect
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.analytics.performance.perf.performanceTracing.strategy.ViewInfo

class XmlViewInfoParser() : ViewInfoParser<View> {
    override fun parse(view: View, depth: Int): List<ViewInfo> {
        val viewInfoList = mutableListOf<ViewInfo>()
        var viewIdString = "unknown"
        try {
            viewIdString = view.resources.getResourceName(view.id)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val isVisible = view.isShown && getViewHeight(view) != 0 && isViewInViewport(view)
        val viewInfo = ViewInfo(view.javaClass.simpleName, viewIdString, isVisible, getViewHeight(view))
        viewInfoList.add(viewInfo)

        try {
            if (view is ViewGroup) {
                for (i in 0 until view.childCount) {
                    viewInfoList.addAll(parse(view.getChildAt(i), depth + 1))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return viewInfoList
    }

    private fun printViewInfo(viewInfo: ViewInfo, depth: Int) {
        val padding = "    ".repeat(depth)
        Log.d("ViewInfo", "$padding${viewInfo.name} - ID: ${viewInfo.resourceIdString}, Visible: ${viewInfo.isVisible}, Height: ${viewInfo.height}")
    }

    private fun isViewInViewport(view: View): Boolean {
        val scrollBounds = Rect()
        view.getHitRect(scrollBounds)

        // Get the dimensions of the screen using application context
        val displayMetrics = view.context.applicationContext.resources.displayMetrics
        val screenHeight = displayMetrics.heightPixels

        // Check if the view intersects with the screen
        return scrollBounds.top < screenHeight && scrollBounds.bottom > 0
    }

    private fun RecyclerView.calculateRecyclerViewHeight(): Int {
        var totalHeight = 0

        for (i in 0 until this.childCount) {
            val child = this.getChildAt(i)
            totalHeight += child.height
        }

        // Add the height of the RecyclerView's padding
        totalHeight += this.paddingTop + this.paddingBottom

        return totalHeight
    }

    private fun getViewHeight(view: View): Int {
        return when (view) {
            is RecyclerView -> view.calculateRecyclerViewHeight()
            else -> view.height
        }
    }
}
