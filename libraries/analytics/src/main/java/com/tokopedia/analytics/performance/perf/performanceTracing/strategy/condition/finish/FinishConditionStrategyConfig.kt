package com.tokopedia.analytics.performance.perf.performanceTracing.strategy.condition.finish

import com.tokopedia.analytics.performance.perf.performanceTracing.strategy.ViewInfo

interface FinishConditionStrategyConfig<T> {
    fun isLayoutFinished(rootView: T, viewInfos: ViewInfo): LayoutStatus
    
    fun timeoutMessage(): String
}
data class LayoutStatus(
    val isFinishedLoading: Boolean,
    val summary: String = ""
)
