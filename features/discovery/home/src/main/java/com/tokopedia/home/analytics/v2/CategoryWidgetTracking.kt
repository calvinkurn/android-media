package com.tokopedia.home.analytics.v2

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.track.TrackApp
import com.tokopedia.track.builder.BaseTrackerBuilder
import com.tokopedia.track.builder.util.BaseTrackerConst

object CategoryWidgetTracking : BaseTrackerConst() {
    private class CustomAction {
        companion object {
            val IMPRESSION_ON_CATEGORY_WIDGET = Action.IMPRESSION_ON.format("category widget banner")
            val CLICK_ON_CATEGORY_WIDGET = Action.CLICK_ON.format("category widget banner")
            const val CLICK_VIEW_ALL_CATEGORY_WIDGET = "click view all on category widget banner"
            const val FORMAT_4_VALUE_UNDERSCORE = "%s_%s_%s_%s"
            const val FORMAT_CAT_ID_NAME = "%s - %s"
        }
    }

    fun getCategoryWidgetBannerImpression(
        banner: List<ChannelGrid>,
        userId: String,
        isToIris: Boolean = false,
        channel: ChannelModel
    ): Map<String, Any> {
        val baseTracker = BaseTrackerBuilder()
        return baseTracker.constructBasicPromotionView(
            event = if (isToIris) Event.PROMO_VIEW_IRIS else Event.PROMO_VIEW,
            eventCategory = Category.HOMEPAGE,
            eventAction = CustomAction.IMPRESSION_ON_CATEGORY_WIDGET,
            eventLabel = channel.channelHeader.name,
            promotions = banner.mapIndexed { position, grid ->
                Promotion(
                    id = CustomAction.FORMAT_4_VALUE_UNDERSCORE.format(channel.id, grid.id, channel.trackingAttributionModel.persoType, channel.trackingAttributionModel.categoryId),
                    name = channel.trackingAttributionModel.promoName,
                    creative = CustomAction.FORMAT_CAT_ID_NAME.format(grid.id, grid.name),
                    position = (position + 1).toString()
                )
            }
        )
            .appendChannelId(channel.id)
            .appendUserId(userId)
            .appendScreen(Screen.DEFAULT)
            .appendCurrentSite(CurrentSite.DEFAULT)
            .appendBusinessUnit(BusinessUnit.DEFAULT).build()
    }

    fun getCategoryWidgetBannerClick(
        userId: String,
        position: String,
        grid: ChannelGrid,
        channel: ChannelModel
    ): Map<String, Any> {
        val baseTracker = BaseTrackerBuilder()
        return baseTracker.constructBasicPromotionClick(
            event = Event.PROMO_CLICK,
            eventCategory = Category.HOMEPAGE,
            eventAction = CustomAction.CLICK_ON_CATEGORY_WIDGET,
            eventLabel = CustomAction.FORMAT_CAT_ID_NAME.format(grid.id, grid.name),
            promotions = listOf(
                Promotion(
                    id = CustomAction.FORMAT_4_VALUE_UNDERSCORE.format(channel.id, grid.id, channel.trackingAttributionModel.persoType, channel.trackingAttributionModel.categoryId),
                    name = channel.trackingAttributionModel.promoName,
                    creative = CustomAction.FORMAT_CAT_ID_NAME.format(grid.id, grid.name),
                    position = position
                )
            )
        )
            .appendAffinity(channel.trackingAttributionModel.persona)
            .appendAttribution(channel.trackingAttributionModel.galaxyAttribution)
            .appendCategoryId(channel.trackingAttributionModel.categoryId)
            .appendShopId(channel.trackingAttributionModel.brandId)
            .appendCampaignCode(channel.trackingAttributionModel.campaignCode)
            .appendChannelId(channel.id)
            .appendUserId(userId)
            .appendScreen(Screen.DEFAULT)
            .appendCurrentSite(CurrentSite.DEFAULT)
            .appendBusinessUnit(BusinessUnit.DEFAULT)
            .build()
    }

    fun sendCategoryWidgetSeeAllClick(channelModel: ChannelModel, userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            DataLayer.mapOf(
                Event.KEY, Event.CLICK_HOMEPAGE,
                Category.KEY, Category.HOMEPAGE,
                Action.KEY, CustomAction.CLICK_VIEW_ALL_CATEGORY_WIDGET,
                Label.KEY, Label.NONE,
                Label.ATTRIBUTION_LABEL, channelModel.channelBanner.attribution,
                Label.AFFINITY_LABEL, channelModel.trackingAttributionModel.persona,
                Label.CATEGORY_LABEL, channelModel.trackingAttributionModel.categoryId,
                Label.SHOP_LABEL, channelModel.trackingAttributionModel.brandId,
                Label.CAMPAIGN_CODE, channelModel.trackingAttributionModel.campaignCode,
                Screen.KEY, Screen.DEFAULT,
                CurrentSite.KEY, CurrentSite.DEFAULT,
                UserId.KEY, userId,
                BusinessUnit.KEY, BusinessUnit.DEFAULT
            )
        )
    }
}
