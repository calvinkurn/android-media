package com.tokopedia.home_component_header.util

import com.tokopedia.home_component_header.model.ChannelHeader

fun ChannelHeader.getLink(): String {
    return if (applink.isNotEmpty()) {
        applink
    } else {
        url
    }
}

fun getHomeComponentHeaderType(): ChannelHeader.HeaderType {
    return if(HomeChannelHeaderRollenceController.isHeaderUsingRollenceVariant()) {
        ChannelHeader.HeaderType.REVAMP
    } else ChannelHeader.HeaderType.CONTROL
}
