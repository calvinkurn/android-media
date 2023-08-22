package com.tokopedia.autocompletecomponent.suggestion.doubleline

import com.tokopedia.discovery.common.reimagine.Search1InstAuto

internal object DoubleLineLayoutStrategyFactory {
    fun create(rollanceType: Search1InstAuto): DoubleLineLayoutStrategy {
        return when (rollanceType) {
            Search1InstAuto.CONTROL -> DoubleLineLayoutStrategyControl()
            else -> DoubleLineLayoutStrategyReimagine()
        }
    }
}
