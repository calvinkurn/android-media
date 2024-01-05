package com.tokopedia.home_component.model

import com.tokopedia.home_component.customview.header.HeaderLayoutStrategy
import com.tokopedia.home_component.customview.header.HeaderLayoutStrategyFactory
import com.tokopedia.home_component.util.getLink

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
) {
    internal val layoutStrategy: HeaderLayoutStrategy = HeaderLayoutStrategyFactory.create(headerType)

    fun getTitleSubtitle(defaultTitle: String, defaultSubtitle: String): Pair<String, String> {
        return if (name.isEmpty()) defaultTitle to defaultSubtitle
        else name to subtitle
    }

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
