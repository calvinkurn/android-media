package com.tokopedia.analytics.performance.perf.performanceTracing.config.strategy.parser.start

import android.view.View
import com.tokopedia.analytics.performance.perf.performanceTracing.config.ViewInfo

class DefaultStartParserStrategy : StartParserStrategyConfig<View> {
    override fun isLayoutReady(rootView: View, views: List<ViewInfo>): Boolean {
        val recyclerViews = views.filter { it.name.contains("RecyclerView") }
        val fullRecyclerView = recyclerViews.find {
            it.height >= (rootView.height * 0.7)
        }
        return fullRecyclerView != null
    }
}
