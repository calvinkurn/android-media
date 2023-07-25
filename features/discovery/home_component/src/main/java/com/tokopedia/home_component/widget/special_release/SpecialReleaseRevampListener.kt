package com.tokopedia.home_component.widget.special_release

import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.productcardgridcarousel.listener.CommonProductCardCarouselListener
import com.tokopedia.home_component_header.view.HomeChannelHeaderListener

/**
 * Created by frenzel
 */
interface SpecialReleaseRevampListener: CommonProductCardCarouselListener {
    fun onShopClicked(channel: ChannelModel, channelGrid: ChannelGrid, applink: String)
    fun onShopImpressed(channel: ChannelModel, channelGrid: ChannelGrid)
    fun onSeeAllClick(channel: ChannelModel, link: String) { }
}
