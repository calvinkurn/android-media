package com.tokopedia.autocompletecomponent.suggestion.doubleline.doublelinerenderstrategy

import com.tokopedia.discovery.common.reimagine.Search1InstAuto

internal object DoubleLineLayoutStrategyFactory {
    fun create(rollanceType: Search1InstAuto): DoubleLineLayoutStrategy {
        return when(rollanceType) {
            Search1InstAuto.CONTROL -> DoubleLineControlLayoutStrategy()
            else -> DoubleLineReimagineVariantLayoutStrategy()
        }
    }
}
