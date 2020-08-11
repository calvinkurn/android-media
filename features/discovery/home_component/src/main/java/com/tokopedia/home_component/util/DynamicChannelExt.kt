package com.tokopedia.home_component.util

import com.tokopedia.home_component.model.ChannelHeader

fun ChannelHeader.getLink(): String {
    return if (applink.isNotEmpty()) {
        applink
    } else {
        url
    }
}