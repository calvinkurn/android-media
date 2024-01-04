package com.tokopedia.home_component.widget.lego3auto

import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.TrackingAttributionModel
import com.tokopedia.home_component.productcardgridcarousel.listener.CommonProductCardCarouselListener

/**
 * Created by frenzel
 */
interface Lego3AutoListener: CommonProductCardCarouselListener {
    fun onItemClicked(trackingAttributionModel: TrackingAttributionModel, channelGrid: ChannelGrid, applink: String)
    fun onItemImpressed(trackingAttributionModel: TrackingAttributionModel, channelGrid: ChannelGrid)
}
