package com.tokopedia.autocompletecomponent.initialstate

import com.tokopedia.discovery.common.reimagine.Search1InstAuto

internal object InitialStateLayoutStrategyFactory {
    fun create(rollanceType: Search1InstAuto): InitialStateLayoutStrategy {
        return when (rollanceType) {
            Search1InstAuto.CONTROL -> InitialStateLayoutStrategyControl()
            else -> InitialStateLayoutStrategyReimagine()
        }
    }
}
