package com.tokopedia.home_component.customview.header

internal object HeaderLayoutStrategyFactory {
    fun create(isRevamp: Boolean) : HeaderLayoutStrategy {
        return if(isRevamp) {
            HeaderRevampLayoutStrategy()
        } else {
            HeaderControlLayoutStrategy()
        }
    }
}
