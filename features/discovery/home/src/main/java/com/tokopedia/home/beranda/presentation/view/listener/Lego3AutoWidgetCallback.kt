package com.tokopedia.home.beranda.presentation.view.listener

import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.TrackingAttributionModel
import com.tokopedia.home_component.widget.lego3auto.Lego3AutoListener

class Lego3AutoWidgetCallback(
    private val homeCategoryListener: HomeCategoryListener,
): Lego3AutoListener {
    override fun onItemClicked(
        trackingAttributionModel: TrackingAttributionModel,
        channelGrid: ChannelGrid,
        applink: String
    ) {
    }

    override fun onItemImpressed(
        trackingAttributionModel: TrackingAttributionModel,
        channelGrid: ChannelGrid
    ) {
    }
}
