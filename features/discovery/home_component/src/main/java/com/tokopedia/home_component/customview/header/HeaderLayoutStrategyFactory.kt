package com.tokopedia.home_component.customview.header

import com.tokopedia.home_component.model.ChannelHeader.HeaderType

internal object HeaderLayoutStrategyFactory {
    fun create(headerType: HeaderType) : HeaderLayoutStrategy {
        return when(headerType) {
            HeaderType.REVAMP -> HeaderRevampLayoutStrategy()
            else -> HeaderControlLayoutStrategy()
        }
    }
}
