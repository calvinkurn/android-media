package com.tokopedia.home_component.widget.special_release

import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.model.TrackingAttributionModel
import com.tokopedia.home_component.productcardgridcarousel.listener.CommonProductCardCarouselListener
import com.tokopedia.home_component_header.view.HomeChannelHeaderListener

/**
 * Created by frenzel
 */
interface SpecialReleaseRevampListener: CommonProductCardCarouselListener {
    fun onShopClicked(trackingAttributionModel: TrackingAttributionModel, channelGrid: ChannelGrid, applink: String)
    fun onShopImpressed(trackingAttributionModel: TrackingAttributionModel, channelGrid: ChannelGrid)
    fun onSeeAllClick(trackingAttributionModel: TrackingAttributionModel, link: String) { }
}
