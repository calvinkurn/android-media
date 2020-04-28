package com.tokopedia.home_component.listener

import com.tokopedia.home_component.model.ChannelModel

interface HomeComponentListener {
    fun onChannelExpired(channelModel: ChannelModel)
}