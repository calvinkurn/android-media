package com.tokopedia.home_component_header.model

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
        var serverTimeOffset: Long = 0,
)
