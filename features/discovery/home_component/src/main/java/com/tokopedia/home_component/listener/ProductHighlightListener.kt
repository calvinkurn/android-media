package com.tokopedia.home_component.listener

import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel

interface ProductHighlightListener{
    fun onProductCardClicked(channel: ChannelModel, channelGrid: ChannelGrid, adapterPosition: Int, applink: String)
}