package com.tokopedia.autocompletecomponent.initialstate

internal object InitialStateLayoutStrategyFactory {
    fun create(isReimagine: Boolean): InitialStateLayoutStrategy {
        return if (isReimagine) InitialStateLayoutStrategyReimagine()
        else InitialStateLayoutStrategyControl()
    }
}
