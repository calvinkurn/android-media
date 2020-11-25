package com.tokopedia.home_component.productcardgridcarousel.listener

import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel

/**
 * @author by yoasfs on 09/06/20
 */

//listener for grid product card
interface CommonProductCardCarouselListener {

    //product card click and impression
    fun onProductCardImpressed(channel: ChannelModel, channelGrid: ChannelGrid, position: Int)
    fun onProductCardClicked(channel: ChannelModel, channelGrid: ChannelGrid, position: Int, applink: String)

    //for see more card click
    fun onSeeMoreCardClicked(channel: ChannelModel, applink: String)

    //for empty card clicked
    fun onEmptyCardClicked(channel: ChannelModel, applink: String, parentPos: Int)
}