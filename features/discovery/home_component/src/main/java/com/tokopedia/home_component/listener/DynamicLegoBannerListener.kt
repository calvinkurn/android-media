package com.tokopedia.home_component.listener

import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel

interface DynamicLegoBannerListener {
    fun onSeeAllSixImage(channelModel: ChannelModel, position: Int)
    fun onSeeAllFourImage(channelModel: ChannelModel, position: Int)
    fun onSeeAllThreemage(channelModel: ChannelModel, position: Int)

    fun onClickGridSixImage(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int, parentPosition: Int)
    fun onClickGridFourImage(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int, parentPosition: Int)
    fun onClickGridThreeImage(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int, parentPosition: Int)
}