@file:SuppressLint("DeprecatedMethod")
@file:Suppress("DEPRECATION")

package com.tokopedia.home.beranda.presentation.view.listener.shorten.item

import android.annotation.SuppressLint
import com.tokopedia.home.analytics.v2.Kd2BannerSquareTracker
import com.tokopedia.home.analytics.v2.Kd2ProductSquareTracker
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.viewholders.shorten.viewholder.listener.ProductWidgetListener
import com.tokopedia.home_component.visitable.shorten.ItemProductWidgetUiModel
import com.tokopedia.home_component.visitable.shorten.ProductWidgetUiModel
import com.tokopedia.track.TrackApp

class ProductTwoSquareWidgetCallback(val listener: HomeCategoryListener) : ProductWidgetListener {

    private var channelModel: ChannelModel? = null

    override fun productChannelHeaderClicked(data: ProductWidgetUiModel) {
        listener.onDynamicChannelClicked(data.header.applink)
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
            Kd2BannerSquareTracker.channelHeaderClicked(
                data.channelModel
            ) as HashMap<String, Any>
        )
    }

    override fun productImpressed(data: ProductWidgetUiModel, position: Int) {
        channelModel = data.channelModel

        val isProduct = data.data.map { it.tracker }.all { it.isProduct() }

        if (isProduct) {
            listener.getTrackingQueueObj()?.putEETracking(
                Kd2ProductSquareTracker.productView(
                    data,
                    listener.userId,
                    position
                ) as HashMap<String, Any>
            )
        } else {
            listener.getTrackingQueueObj()?.putEETracking(
                Kd2BannerSquareTracker.widgetImpressed(
                    data.channelModel,
                    listener.userId,
                    position
                ) as HashMap<String, Any>
            )
        }
    }

    override fun itemProductClicked(data: ItemProductWidgetUiModel, position: Int) {
        listener.onDynamicChannelClicked(data.appLink)

        if (data.tracker.isProduct()) {
            channelModel?.let {
                TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                    Kd2ProductSquareTracker.productClick(
                        it,
                        data.tracker,
                        listener.userId,
                        position
                    )
                )
            }

            channelModel = null
        } else {
            TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                Kd2BannerSquareTracker.cardClicked(
                    data.tracker,
                    listener.userId,
                    position
                ) as HashMap<String, Any>
            )
        }
    }

    override fun retryWidget() {
        listener.refreshShortenWidget()
    }
}
