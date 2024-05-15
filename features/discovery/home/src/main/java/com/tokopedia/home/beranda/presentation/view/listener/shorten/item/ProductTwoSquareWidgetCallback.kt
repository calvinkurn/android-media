@file:SuppressLint("DeprecatedMethod")
@file:Suppress("DEPRECATION")

package com.tokopedia.home.beranda.presentation.view.listener.shorten.item

import android.annotation.SuppressLint
import com.tokopedia.home.analytics.v2.Kd2SquareTracker
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home_component.viewholders.shorten.viewholder.listener.ProductWidgetListener
import com.tokopedia.home_component.visitable.shorten.ItemProductWidgetUiModel
import com.tokopedia.home_component.visitable.shorten.ProductWidgetUiModel
import com.tokopedia.track.TrackApp

class ProductTwoSquareWidgetCallback(val listener: HomeCategoryListener) : ProductWidgetListener {

    override fun productChannelHeaderClicked(data: ProductWidgetUiModel) {
        listener.onDynamicChannelClicked(data.header.applink)
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
            Kd2SquareTracker.channelHeaderClicked(
                data.channelModel
            ) as HashMap<String, Any>
        )
    }

    override fun productImpressed(data: ProductWidgetUiModel, position: Int) {
        listener.getTrackingQueueObj()?.putEETracking(
            Kd2SquareTracker.widgetImpressed(
                data.channelModel,
                listener.userId,
                position
            ) as HashMap<String, Any>
        )
    }

    override fun itemProductClicked(data: ItemProductWidgetUiModel, position: Int) {
        listener.onDynamicChannelClicked(data.appLink)
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
            Kd2SquareTracker.cardClicked(
                data.tracker,
                listener.userId,
                position
            ) as HashMap<String, Any>
        )
    }

    override fun retryWidget() {
        listener.refreshShortenWidget()
    }
}
