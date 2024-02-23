package com.tokopedia.home.beranda.presentation.view.listener

import android.content.Context
import com.tokopedia.home.analytics.v2.Kd4SquareTracker
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home_component.listener.Kd4SquareWidgetListener
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.track.TrackApp

class Kd4WidgetCallback constructor(
    val context: Context?,
    val homeCategoryListener: HomeCategoryListener
) : Kd4SquareWidgetListener {

    override fun onWidgetImpressed(channel: ChannelModel, position: Int) {
        homeCategoryListener.getTrackingQueueObj()?.putEETracking(
            Kd4SquareTracker.widgetImpressed(
                channel,
                homeCategoryListener.userId,
                position
            ) as HashMap<String, Any>
        )
    }

    override fun onCardClicked(channel: ChannelModel, channelGrid: ChannelGrid, position: Int) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
            Kd4SquareTracker.cardClicked(
                channel,
                channelGrid,
                homeCategoryListener.userId,
                position
            ) as HashMap<String, Any>
        )
    }

    override fun onViewAllChevronClicked(channel: ChannelModel) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
            Kd4SquareTracker.viewAllChevronClicked(channel) as HashMap<String, Any>
        )
    }
}
