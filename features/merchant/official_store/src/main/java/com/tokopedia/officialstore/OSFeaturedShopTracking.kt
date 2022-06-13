package com.tokopedia.officialstore

import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.track.builder.BaseTrackerBuilder
import com.tokopedia.track.builder.util.BaseTrackerConst

object OSFeaturedShopTracking: BaseTrackerConst() {

    private val OS_MICROSITE = "os microsite"
    private val CLICK_SHOP_WIDGET = "click banner - dynamic channel shop"
    private val IMPRESSION_SHOP_WIDGET = "impression banner - dynamic channel shop"

    //dynamic channel shop - {shop_id} - {header_name} - {category_name}
    private val SHOP_WIDGET_LABEL = "dynamic channel shop - %s - %s - %s"

    //{banner_id}_{shop_id}
    private val SHOP_ID = "%s_%s"

    //official-store/{category name} - dynamic channel shop - {destination_applink} - {header_name}
    private val SHOP_NAME = "/official-store/%s - dynamic channel shop - %s - %s"

    fun getEventClickShopWidget(channel: ChannelModel, grid: ChannelGrid, categoryName: String, bannerPosition: Int, userId: String) =
            BaseTrackerBuilder()
                    .constructBasicPromotionClick(
                            event = Event.PROMO_CLICK,
                            eventAction = CLICK_SHOP_WIDGET,
                            eventCategory = OS_MICROSITE,
                            eventLabel = SHOP_WIDGET_LABEL.format(grid.shop.id, channel.channelBanner.title, categoryName),
                            promotions = listOf(createFeaturedShopPromotion(
                                    channel = channel,
                                    channelGrid = grid,
                                    position = (bannerPosition+1),
                                    categoryName = categoryName
                            )))
                    .appendBusinessUnit(BusinessUnit.DEFAULT)
                    .appendCurrentSite(CurrentSite.DEFAULT)
                    .appendUserId(userId)
                    .build()

    fun getEventImpressionShopWidget(channel: ChannelModel, grid: ChannelGrid, categoryName: String, bannerPosition: Int, userId: String) =
            BaseTrackerBuilder()
                    .constructBasicPromotionView(
                            event = Event.PROMO_VIEW,
                            eventAction = IMPRESSION_SHOP_WIDGET,
                            eventCategory = OS_MICROSITE,
                            eventLabel = SHOP_WIDGET_LABEL.format(grid.shop.id, channel.channelBanner.title, categoryName),
                            promotions = listOf(createFeaturedShopPromotion(
                                    channel = channel,
                                    channelGrid = grid,
                                    position = (bannerPosition+1),
                                    categoryName = categoryName
                            )))
                    .appendBusinessUnit(BusinessUnit.DEFAULT)
                    .appendCurrentSite(CurrentSite.DEFAULT)
                    .appendUserId(userId)
                    .build()

    private fun createFeaturedShopPromotion(channel: ChannelModel, channelGrid: ChannelGrid, position: Int, categoryName: String): Promotion{
        return Promotion(
                id = SHOP_ID.format(channelGrid.id, channelGrid.shop.id),
                name = SHOP_NAME.format(categoryName, channelGrid.applink, channel.channelBanner.title),
                position = position.toString(),
                creative = channelGrid.attribution,
        )

    }
}