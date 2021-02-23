package com.tokopedia.home.beranda.presentation.view.listener

import com.tokopedia.home.analytics.v2.ProductHighlightTracking
import com.tokopedia.home.analytics.v2.hasLabelGroupFulfillment
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home_component.listener.ProductHighlightListener
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import java.util.HashMap

class ProductHighlightComponentCallback(val homeCategoryListener: HomeCategoryListener): ProductHighlightListener{
    override fun onProductCardClicked(channel: ChannelModel, channelGrid: ChannelGrid, adapterPosition: Int, applink: String) {
        homeCategoryListener.onSectionItemClicked(channelGrid.applink)
        ProductHighlightTracking.sendRecommendationListClick(
                channelId = channel.id,
                headerName = channel.channelHeader.name,
                campaignCode = channel.trackingAttributionModel.campaignCode,
                persoType = channel.trackingAttributionModel.persoType,
                categoryId = channel.trackingAttributionModel.categoryId,
                gridFreeOngkirIsActive = channelGrid.isFreeOngkirActive && !channelGrid.labelGroup.hasLabelGroupFulfillment(),
                gridFreeOngkirExtraIsActive = channelGrid.isFreeOngkirActive && channelGrid.labelGroup.hasLabelGroupFulfillment(),
                gridId = channelGrid.id,
                gridName = channelGrid.name,
                gridPrice = channelGrid.price,
                position = adapterPosition,
                isTopAds = channelGrid.isTopads,
                pageName = channel.pageName,
                recommendationType = channelGrid.recommendationType,
                positionOnHome = adapterPosition
        )
    }

    override fun onProductCardImpressed(channel: ChannelModel, channelGrid: ChannelGrid, adapterPosition: Int) {
        //GA
        homeCategoryListener.getTrackingQueueObj()?.putEETracking(
                ProductHighlightTracking.getProductHighlightImpression(
                        channel,
                        userId = homeCategoryListener.userId,
                        positionOnHome = adapterPosition) as HashMap<String, Any>
        )
        //iris
        homeCategoryListener.putEEToIris(ProductHighlightTracking.getProductHighlightImpression(
                channel, homeCategoryListener.userId, true, positionOnHome = adapterPosition
        ) as HashMap<String, Any>)

    }
}