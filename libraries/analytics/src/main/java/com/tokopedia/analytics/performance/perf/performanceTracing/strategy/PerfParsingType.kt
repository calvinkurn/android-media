package com.tokopedia.analytics.performance.perf.performanceTracing.strategy

import android.view.View

sealed class PerfParsingType {

    data class XML(val parsingStrategy: ParsingStrategy<View>) : PerfParsingType()
//    data class Compose(val parsingStrategy: ParsingStrategy<Composable>) : PerfParsingType()
}
