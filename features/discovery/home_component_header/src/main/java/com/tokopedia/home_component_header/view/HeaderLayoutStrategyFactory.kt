package com.tokopedia.home_component_header.view

internal object HeaderLayoutStrategyFactory {
    fun create(isRevamp: Boolean) : HeaderLayoutStrategy {
        return if(isRevamp) {
            HeaderRevampLayoutStrategy()
        } else {
            HeaderControlLayoutStrategy()
        }
    }
}
