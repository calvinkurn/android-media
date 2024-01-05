package com.tokopedia.analytics.performance.perf.performanceTracing.strategy.condition.finish

import android.view.View
import com.tokopedia.analytics.performance.perf.performanceTracing.strategy.ViewInfo

class FullRecyclerViewPageFinishConditionStrategy : FinishConditionStrategyConfig<View> {
    override fun isLayoutFinished(rootView: View, views: ViewInfo): LayoutStatus {
        if (rootView.height == 0) return LayoutStatus(false, "RootView height is 0")

        val visibleLoadableView = containsLoader(views)
        val fullRecyclerView = getFullRecyclerView(views, rootView)
        val layoutFinished = !visibleLoadableView && fullRecyclerView != null
        return LayoutStatus(layoutFinished, generateLayoutSummary(fullRecyclerView, rootView))
    }

    override fun timeoutMessage(): String {
        return "err: Parsing Timeout. No recyclerview with full height detected, try changing " +
            "different finish condition strategy for your page."
    }

    private fun generateLayoutSummary(
        fullRecyclerView: ViewInfo?,
        rootView: View
    ): String {
        var summary = "" +
            "Perf trace finished, full recyclerview detected:\n" +
            "Id: ${fullRecyclerView?.tag}\n" +
            "Height: ${fullRecyclerView?.height} (Viewport height: ${rootView.height})\n\n" +
            "Visible item: ${fullRecyclerView?.directChilds?.filter { it.isVisible }?.size}\n\n" +
            "List of view shown in full recyclerview: "

        fullRecyclerView?.directChilds?.forEach {
            if (it.isVisible) {
                val desc = "\n - ${it.name}, tag: ${it.tag}"
                summary += limitAndAddEllipsis(desc)
            }
        }
        return summary
    }

    private fun limitAndAddEllipsis(input: String): String {
        val maxLength = 50
        return if (input.length <= maxLength) {
            "$input"
        } else {
            "${input.substring(0, maxLength - 3)}..."
        }
    }

    fun containsLoader(viewInfo: ViewInfo): Boolean {
        if (viewInfo.name.contains("Loader", ignoreCase = true) && viewInfo.isVisible) {
            return true
        }

        for (child in viewInfo.directChilds) {
            if (containsLoader(child)) {
                return true
            }
        }

        return false
    }

    fun getFullRecyclerView(viewInfo: ViewInfo, rootView: View): ViewInfo? {
        if (viewInfo.name.contains("RecyclerView", ignoreCase = true) &&
            viewInfo.isVisible &&
            viewInfo.height >= (rootView.height * 0.5) &&
            viewInfo.directChilds.size > 1 &&
            !viewInfo.directChilds.any { it.height == viewInfo.height }
        ) {
            return viewInfo
        }

        for (child in viewInfo.directChilds) {
            val matchingRecyclerView = getFullRecyclerView(child, rootView)
            if (matchingRecyclerView != null) {
                return matchingRecyclerView
            }
        }

        return null
    }
}
