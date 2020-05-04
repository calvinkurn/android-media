package com.tokopedia.home_component.listener

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home_component.model.ChannelModel

interface HomeComponentListener {
    fun onChannelExpired(channelModel: ChannelModel, channelPosition: Int, visitable: Visitable<*>)
}