package com.tokopedia.home_component.listener

import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel

/**
 * @author by yoasfs on 09/06/20
 */
interface MixLeftComponentListener {
    fun onMixLeftImpressed(channel: ChannelModel, parentPos: Int)

    fun onImageBannerImpressed(channelModel: ChannelModel, position: Int)
    fun onImageBannerClicked(channelModel: ChannelModel, position: Int, applink: String)
    fun onSeeAllBannerClicked(channel: ChannelModel, applink: String)

    //product card click and impression
    fun onProductCardImpressed(channel: ChannelModel, channelGrid: ChannelGrid, adapterPosition: Int, position: Int)
    fun onProductCardClicked(channel: ChannelModel, channelGrid: ChannelGrid, adapterPosition: Int, position: Int, applink: String)

    //for see more card click
    fun onSeeMoreCardClicked(channel: ChannelModel, applink: String)

    //for empty card clicked
    fun onEmptyCardClicked(channel: ChannelModel, applink: String, parentPos: Int)
}