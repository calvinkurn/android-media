package com.tokopedia.home_component.customview

import com.tokopedia.home_component.model.ChannelModel

interface HeaderListener {
    fun onSeeAllClick(link: String)
    fun onChannelExpired(channelModel: ChannelModel)
}