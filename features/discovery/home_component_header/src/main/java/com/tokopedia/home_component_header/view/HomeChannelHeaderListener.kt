package com.tokopedia.home_component_header.view

interface HomeChannelHeaderListener {
    fun onSeeAllClick(link: String) { }
    fun onChannelExpired(channelId: String) { }
    fun onReloadClick(channelId: String) { }
    fun onDismissClick(channelId: String) { }
}
