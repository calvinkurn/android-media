package com.tokopedia.home_component.listener

import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel

interface Kd4SquareWidgetListener {

    fun onWidgetImpressed(channel: ChannelModel, position: Int)
    fun onCardClicked(channel: ChannelModel, channelGrid: ChannelGrid, position: Int)
    fun onViewAllChevronClicked(channel: ChannelModel)
}
