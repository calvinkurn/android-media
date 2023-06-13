package com.tokopedia.home_component.listener

import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel

/**
 * @author by yoasfs on 09/06/20
 */
interface MixTopComponentListener {
    fun onMixTopImpressed(channel: ChannelModel, parentPos: Int)

    fun onSeeAllHeaderClicked(channel: ChannelModel, applink: String)
    fun onMixtopButtonClicked(channel: ChannelModel)
    fun onSectionItemClicked(applink: String)
    fun onBackgroundClicked(channel: ChannelModel)

    //product card click and impression
    fun onProductCardImpressed(channel: ChannelModel, channelGrid: ChannelGrid, adapterPosition: Int, position: Int)
    fun onProductCardClicked(channel: ChannelModel, channelGrid: ChannelGrid, adapterPosition: Int, position: Int, applink: String)

    //for see more card click
    fun onSeeMoreCardClicked(channel: ChannelModel, applink: String)
}
