package com.tokopedia.analytics.performance.perf.performanceTracing.strategy.parser.start

import android.view.View
import com.tokopedia.analytics.performance.perf.performanceTracing.strategy.ViewInfo

class FullRecyclerViewPageStartParser : StartParserStrategyConfig<View> {
    override fun isLayoutReady(rootView: View, views: List<ViewInfo>): Boolean {
        if (rootView.height == 0) return false
        val recyclerViews = views.filter { it.name.contains("RecyclerView") }
        val fullRecyclerView = recyclerViews.find {
            it.height >= (rootView.height * 0.9)
        }
        return fullRecyclerView != null
    }
}
