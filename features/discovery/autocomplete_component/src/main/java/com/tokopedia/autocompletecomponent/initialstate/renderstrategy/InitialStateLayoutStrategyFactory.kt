package com.tokopedia.autocompletecomponent.initialstate.renderstrategy

import com.tokopedia.discovery.common.reimagine.Search1InstAuto

internal object InitialStateLayoutStrategyFactory {
    fun create(rollanceType: Search1InstAuto): InitialStateRenderStrategy {
        return when (rollanceType) {
            Search1InstAuto.CONTROL -> InitialStateControlLayoutStrategy()
            else -> InitialStateReimagineLayoutStrategy()
        }
    }
}
