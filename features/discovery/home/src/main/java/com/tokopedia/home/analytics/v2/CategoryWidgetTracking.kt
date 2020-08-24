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
            const val FORMAT_4_VALUE_UNDERSCORE = "%s_%s_%s_%s";
            const val FORMAT_CAT_ID_NAME = "%s - %s";
        }
    }

    fun getCategoryWidgetBannerImpression(
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
            screen = Screen.DEFAULT,
            currentSite = CurrentSite.DEFAULT,
            businessUnit = BusinessUnit.DEFAULT,
            promotions = banner.mapIndexed { position, grid ->
                Promotion(
                        id = CustomAction.FORMAT_4_VALUE_UNDERSCORE.format(channel.id, grid.id, channel.persoType, channel.categoryID),
                        name = channel.promoName,
                        creative = CustomAction.FORMAT_CAT_ID_NAME.format(grid.id, grid.name),
                        position = (position+1).toString()
                )
            }
    )

    fun getCategoryWidgetBannerClick(
            channelId: String,
            userId: String,
            position: String,
            grid: DynamicHomeChannel.Grid,
            channel: DynamicHomeChannel.Channels
    ) = getBasicPromotionChannelClick(
            event = Event.PROMO_CLICK,
            eventCategory = Category.HOMEPAGE,
            eventAction = CustomAction.CLICK_ON_CATEGORY_WIDGET,
            eventLabel = CustomAction.FORMAT_CAT_ID_NAME.format(grid.id, grid.name),
            channelId = channelId,
            affinity = channel.persona,
            attribution = channel.galaxyAttribution,
            categoryId = channel.categoryID,
            shopId = channel.brandId,
            userId = userId,
            campaignCode = channel.campaignCode,
            screen = Screen.DEFAULT,
            currentSite = CurrentSite.DEFAULT,
            businessUnit = BusinessUnit.DEFAULT,
            promotions = listOf(
                    Promotion(
                            id = CustomAction.FORMAT_4_VALUE_UNDERSCORE.format(channel.id, grid.id, channel.persoType, channel.categoryID),
                            name = channel.promoName,
                            creative = CustomAction.FORMAT_CAT_ID_NAME.format(grid.id, grid.name),
                            position = position
                    )
            )
    )

    fun sendCategoryWidgetSeeAllClick(channelModel: DynamicHomeChannel.Channels, userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                DataLayer.mapOf(
                        Event.KEY, Event.CLICK_HOMEPAGE,
                        Category.KEY, Category.HOMEPAGE,
                        Action.KEY, CustomAction.CLICK_VIEW_ALL_CATEGORY_WIDGET,
                        Label.KEY, Label.NONE,
                        Label.ATTRIBUTION_LABEL, channelModel.banner.attribution,
                        Label.AFFINITY_LABEL, channelModel.persona,
                        Label.CATEGORY_LABEL, channelModel.categoryID,
                        Label.SHOP_LABEL, channelModel.brandId,
                        Label.CAMPAIGN_CODE, channelModel.campaignCode,
                        Screen.KEY, Screen.DEFAULT,
                        CurrentSite.KEY, CurrentSite.DEFAULT,
                        UserId.KEY, userId,
                        BusinessUnit.KEY, BusinessUnit.DEFAULT)
        )
    }
}