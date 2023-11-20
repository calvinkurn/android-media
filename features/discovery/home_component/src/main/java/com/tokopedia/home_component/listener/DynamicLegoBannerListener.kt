package com.tokopedia.home_component.listener

import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel

interface DynamicLegoBannerListener {
    fun onSeeAllSixImage(channelModel: ChannelModel, position: Int)
    fun onSeeAllFourImage(channelModel: ChannelModel, position: Int)
    fun onSeeAllThreemage(channelModel: ChannelModel, position: Int)
    fun onSeeAllTwoImage(channelModel: ChannelModel, position: Int)

    fun onClickGridSixImage(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int, parentPosition: Int)
    fun onClickGridFourImage(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int, parentPosition: Int)
    fun onClickGridThreeImage(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int, parentPosition: Int)
    fun onClickGridTwoImage(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int, parentPosition: Int)

    // Per grid impression listener
    fun onImpressionGridSixImage(channelModel: ChannelModel, parentPosition: Int) { }
    fun onImpressionGridFourImage(channelModel: ChannelModel, parentPosition: Int) { }
    fun onImpressionGridThreeImage(channelModel: ChannelModel, parentPosition: Int) { }
    fun onImpressionGridTwoImage(channelModel: ChannelModel, parentPosition: Int) { }

    // Viewport impression listener
    fun onChannelImpressionSixImage(channelModel: ChannelModel, parentPosition: Int)
    fun onChannelImpressionFourImage(channelModel: ChannelModel, parentPosition: Int)
    fun onChannelImpressionThreeImage(channelModel: ChannelModel, parentPosition: Int)
    fun onChannelImpressionTwoImage(channelModel: ChannelModel, parentPosition: Int)

    // Globalized viewport impression listener for new tracking mechanism
    fun onViewportImpression(channelModel: ChannelModel) { }

    fun getDynamicLegoBannerData(channelModel: ChannelModel)
}
