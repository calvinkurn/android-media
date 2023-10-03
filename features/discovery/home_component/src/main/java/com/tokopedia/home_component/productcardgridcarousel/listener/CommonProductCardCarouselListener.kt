package com.tokopedia.home_component.productcardgridcarousel.listener

import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.model.TrackingAttributionModel

/**
 * @author by yoasfs on 09/06/20
 */

//listener for grid product card
interface CommonProductCardCarouselListener {

    //product card click and impression
    @Deprecated("pass TrackingAttributionModel instead")
    fun onProductCardImpressed(channel: ChannelModel, channelGrid: ChannelGrid, position: Int) { }
    fun onProductCardImpressed(trackingAttributionModel: TrackingAttributionModel, channelGrid: ChannelGrid, position: Int) { }
    @Deprecated("pass TrackingAttributionModel instead")
    fun onProductCardClicked(channel: ChannelModel, channelGrid: ChannelGrid, position: Int, applink: String) { }
    fun onProductCardClicked(trackingAttributionModel: TrackingAttributionModel, channelGrid: ChannelGrid, position: Int, applink: String) { }

    //for see more card click
    @Deprecated("pass TrackingAttributionModel instead")
    fun onSeeMoreCardClicked(channel: ChannelModel, applink: String) { }
    fun onSeeMoreCardClicked(trackingAttributionModel: TrackingAttributionModel, applink: String) { }

    //for empty card clicked
    @Deprecated("pass TrackingAttributionModel instead")
    fun onEmptyCardClicked(channel: ChannelModel, applink: String, parentPos: Int) { }
    fun onEmptyCardClicked(trackingAttributionModel: TrackingAttributionModel, applink: String, parentPos: Int) { }
}
