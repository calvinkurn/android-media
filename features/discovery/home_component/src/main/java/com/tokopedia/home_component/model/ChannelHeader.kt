package com.tokopedia.home_component.model

import com.tokopedia.home_component.customview.header.HeaderLayoutStrategy
import com.tokopedia.home_component.customview.header.HeaderLayoutStrategyFactory

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
        val useHeaderRevamp: Boolean = false,
) {
    internal val layoutStrategy: HeaderLayoutStrategy = HeaderLayoutStrategyFactory.create(useHeaderRevamp)

    fun getTitleSubtitle(defaultTitle: String, defaultSubtitle: String): Pair<String, String> {
        return if (name.isEmpty()) defaultTitle to defaultSubtitle
        else name to subtitle
    }
}
