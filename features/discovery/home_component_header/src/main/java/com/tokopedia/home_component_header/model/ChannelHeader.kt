package com.tokopedia.home_component_header.model

import com.tokopedia.home_component_header.view.HeaderLayoutStrategy
import com.tokopedia.home_component_header.view.HeaderLayoutStrategyFactory

data class ChannelHeader(
    val id: String = "",
    val name: String = "",
    val subtitle: String = "",
    val expiredTime: String = "",
    val serverTimeUnix: Long = 0,
    val applink: String = "",
    val url: String = "",
    val backColor: String = "",
    val backImage: String = "",
    val textColor: String = "",
        //  Data from channel level
    val channelId: String = "",
    val serverTimeOffset: Long = 0,
    val headerType: HeaderType = HeaderType.CONTROL,
    val iconSubtitleUrl: String = "",
    val pageSource: PageSource = PageSource.HOME
) {
    internal val layoutStrategy: HeaderLayoutStrategy = HeaderLayoutStrategyFactory.create(headerType)

    enum class HeaderType {
        CONTROL,
        REVAMP
    }

    enum class PageSource {
        HOME,
        SRP
    }
}
