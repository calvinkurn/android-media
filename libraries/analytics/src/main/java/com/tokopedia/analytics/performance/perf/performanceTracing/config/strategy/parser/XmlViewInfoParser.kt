package com.tokopedia.analytics.performance.perf.performanceTracing.config.strategy.parser

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.analytics.performance.perf.performanceTracing.config.ViewInfo

class XmlViewInfoParser() : ViewInfoParser<View> {
    override fun parse(view: View): List<ViewInfo> {
        val viewInfoList = mutableListOf<ViewInfo>()
        var viewIdString = "unknown"
        try {
            viewIdString = view.resources.getResourceName(view.id)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val isVisible = view.visibility == View.VISIBLE && getViewHeight(view) != 0
        val viewInfo = ViewInfo(view.javaClass.simpleName, viewIdString, isVisible, getViewHeight(view))
        viewInfoList.add(viewInfo)

        try {
            if (view is ViewGroup) {
                for (i in 0 until view.childCount) {
                    viewInfoList.addAll(parse(view.getChildAt(i)))
                }
            }
        } catch (e: Exception) {
            if (view is ViewGroup) {
                for (i in 0 until view.childCount) {
                    viewInfoList.addAll(parse(view.getChildAt(i)))
                }
            }
        }
        return viewInfoList
    }

    private fun RecyclerView.calculateRecyclerViewHeight(): Int {
        var totalHeight = 0

        for (i in 0 until this.childCount) {
            val child = this.getChildAt(i)
            totalHeight += getViewHeight(child)
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
