package com.tokopedia.home.analytics.v2

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.track.TrackApp

object CategoryWidgetTracking : BaseTracking() {
    private class CustomAction{
        companion object {
            val IMPRESSION_ON_CATEGORY_WIDGET = Action.IMPRESSION_ON.format("category widget banner")
            val CLICK_ON_CATEGORY_WIDGET = Action.CLICK_ON.format("category widget banner")
            const val CLICK_VIEW_ALL_CATEGORY_WIDGET = "click view all on category widget banner"
        }
    }

    fun getCategoryWidgetBanneImpression(
            banner: List<DynamicHomeChannel.Grid>,
            userId: String,
            isToIris: Boolean = false,
            channel: DynamicHomeChannel.Channels
    ) = getBasicPromotionChannelView(
            event = if (isToIris) Event.PROMO_VIEW_IRIS else Event.PROMO_VIEW,
            eventCategory = Category.HOMEPAGE,
            eventAction = CustomAction.IMPRESSION_ON_CATEGORY_WIDGET,
            eventLabel = channel.header.name,
            channelId = channel.id,
            userId = userId,
            promotions = banner.mapIndexed { position, grid ->
                Promotion(
                        id = grid.id,
                        name = channel.promoName,
                        creative = grid.attribution,
                        position = (position+1).toString()
                )
            }
    )

    fun getCategoryWidgetBannerClick(
            headerName: String,
            channelId: String,
            position: String,
            grid: DynamicHomeChannel.Grid,
            channel: DynamicHomeChannel.Channels
    ) = getBasicPromotionChannelClick(
            event = Event.PROMO_CLICK,
            eventCategory = Category.HOMEPAGE,
            eventAction = CustomAction.CLICK_ON_CATEGORY_WIDGET,
            eventLabel = headerName,
            channelId = channelId,
            affinity = channel.persona,
            attribution = channel.galaxyAttribution,
            categoryId = channel.categoryID,
            shopId = channel.brandId,
            campaignCode = channel.campaignCode,
            promotions = listOf(
                    Promotion(
                            id = grid.id,
                            name = channel.promoName,
                            creative = grid.attribution,
                            position = position
                    )
            )
    )

    fun sendCategoryWidgetSeeAllClick(headerName: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                DataLayer.mapOf(
                Event.KEY, Event.CLICK_HOMEPAGE,
                Category.KEY, Category.HOMEPAGE,
                Action.KEY, CustomAction.CLICK_VIEW_ALL_CATEGORY_WIDGET,
                Label.KEY, headerName
        )
        )
    }
}