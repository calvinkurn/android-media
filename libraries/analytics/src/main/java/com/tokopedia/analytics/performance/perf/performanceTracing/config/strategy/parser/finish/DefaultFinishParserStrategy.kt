package com.tokopedia.analytics.performance.perf.performanceTracing.config.strategy.parser.finish

import android.view.View
import com.tokopedia.analytics.performance.perf.performanceTracing.config.ViewInfo

class DefaultFinishParserStrategy : FinishParserStrategyConfig<View> {
    override fun isLayoutFinished(rootView: View, views: List<ViewInfo>): Boolean {
        val visibleLoadableView = views.filter { it.name.contains("Loader") && it.isVisible }

        val recyclerViews = views.filter { it.name.contains("RecyclerView") }
        val fullRecyclerView = recyclerViews.find {
            it.height >= (rootView.height * 0.9)
        }

        return visibleLoadableView.isEmpty() && fullRecyclerView != null
    }
}
