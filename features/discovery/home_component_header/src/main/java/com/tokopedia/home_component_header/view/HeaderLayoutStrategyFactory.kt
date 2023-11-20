package com.tokopedia.home_component_header.view

import com.tokopedia.home_component_header.model.ChannelHeader.HeaderType

internal object HeaderLayoutStrategyFactory {
    fun create(headerType: HeaderType) : HeaderLayoutStrategy {
        return when(headerType) {
            HeaderType.REVAMP -> HeaderRevampLayoutStrategy()
            else -> HeaderControlLayoutStrategy()
        }
    }
}
