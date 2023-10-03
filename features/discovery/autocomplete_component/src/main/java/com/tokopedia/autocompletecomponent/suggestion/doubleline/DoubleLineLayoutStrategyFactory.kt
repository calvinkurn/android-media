package com.tokopedia.autocompletecomponent.suggestion.doubleline

internal object DoubleLineLayoutStrategyFactory {
    fun create(isReimagine: Boolean): DoubleLineLayoutStrategy {
        return if (isReimagine) DoubleLineLayoutStrategyReimagine()
        else DoubleLineLayoutStrategyControl()
    }
}
