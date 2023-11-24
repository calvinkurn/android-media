package com.tokopedia.home_component_header.model

import com.tokopedia.home_component_header.util.getLink
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
    val iconSubtitleUrl: String = "",
    val headerType: HeaderType = HeaderType.CHEVRON,
    //  Data from channel level
    val channelId: String = "",
    val serverTimeOffset: Long = 0,
) {
    internal val layoutStrategy: HeaderLayoutStrategy = HeaderLayoutStrategyFactory.create(headerType)

    fun hasSeeMoreApplink(): Boolean {
        return getLink().isNotEmpty()
    }

    fun hasTitleOnly(): Boolean {
        return subtitle.isEmpty() &&
            expiredTime.isEmpty() &&
            iconSubtitleUrl.isEmpty()
    }

    enum class HeaderType {
        CHEVRON
    }
}
