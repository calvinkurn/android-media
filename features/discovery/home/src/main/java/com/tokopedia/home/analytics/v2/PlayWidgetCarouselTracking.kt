package com.tokopedia.home.analytics.v2

import com.tokopedia.analyticconstant.DataLayer

object PlayWidgetCarouselTracking : BaseTracking() {
    private object CustomEvent{
        const val CLICK_HOMEPAGE = "clickHomepage"
        const val FORMAT_4_VALUE_UNDERSCORE = "%s_%s_%s_%s";
    }

    private const val HOMEPAGE_CMP = "homepage-cmp"
    private const val CLICK_OTHER_CONTENT = "click other content"
    private const val CLICK_VIEW_ALL = "click view all"
    private const val CLICK_REMIND_ME = "click remind me"
    private const val CLICK_REMOVE_REMIND_ME = "click on remove remind me"
    private const val CLICK = "click"
    private const val PROMO_BANNER_NAME = "/ - p%s - play sgc banner - %s"
    private const val PROMO_CHANNEL_NAME = "/ - p%s - play sgc channel - %s"
    private const val EVENT_LABEL_BANNER_PLAY = "click on banner play - %s - %s"
    private const val EVENT_LABEL_CONTENT_PLAY = "click channel - %s - %s - %s - %s - %s"
    private const val EVENT_LABEL_CLICK_VIEW_ALL = "0 - Tokopedia Play"
    private const val SGC_BANNER = "play sgc banner"
    private const val SGC_CHANNEL = "play sgc channel"

    fun getClickLeftBanner(
            channelId: String,
            userId: String,
            bannerId: String,
            shopName: String,
            creativeName: String,
            positionFold: String,
            widgetPosition: String,
            position: String,
            promoCode: String
    ) = getBasicPromotionChannelClick(
            event = Event.PROMO_CLICK,
            eventCategory = HOMEPAGE_CMP,
            eventAction = CLICK,
            eventLabel = EVENT_LABEL_BANNER_PLAY.format(creativeName, positionFold),
            channelId = channelId,
            userId = userId,
            promotions = listOf(
                    Promotion(
                            id = bannerId,
                            name = PROMO_BANNER_NAME.format(widgetPosition, shopName),
                            creative = creativeName,
                            promoCodes = promoCode,
                            position = position
                    )
            )
    ) as HashMap<String, Any>

    fun getImpressionLeftBanner(
            channelId: String,
            userId: String,
            bannerId: String,
            shopName: String,
            creativeName: String,
            positionFold: String,
            widgetPosition: String,
            position: String,
            promoCode: String
    ): HashMap<String, Any> = getBasicPromotionChannelView(
            event = Event.PROMO_VIEW,
            eventCategory = HOMEPAGE_CMP,
            eventAction = Action.IMPRESSION_ON.format(SGC_BANNER),
            eventLabel = "%s - %s".format(creativeName, positionFold),
            channelId = channelId,
            userId = userId,
            promotions = listOf(
                    Promotion(
                            id = bannerId,
                            name = PROMO_BANNER_NAME.format(widgetPosition, shopName),
                            creative = creativeName,
                            promoCodes = promoCode,
                            position = position
                    )
            )
    ) as HashMap<String, Any>

    fun getImpressionBanner(
            channelId: String,
            userId: String,
            bannerId: String,
            shopId: String,
            creativeName: String,
            widgetPosition: String,
            position: String,
            positionFold: String,
            autoPlay: String,
            channelName: String
    ):HashMap<String, Any> = getBasicPromotionChannelView(
            event = Event.PROMO_VIEW,
            eventCategory = HOMEPAGE_CMP,
            eventAction = Action.IMPRESSION_ON.format(SGC_CHANNEL),
            eventLabel = "%s - %s - %s - %s - %s".format(shopId, channelId, position, positionFold, autoPlay),
            userId = userId,
            channelId = channelId,
            promotions = listOf(
                    Promotion(
                            id = bannerId,
                            name = PROMO_CHANNEL_NAME.format(widgetPosition, channelName),
                            creative = creativeName,
                            position = position
                    )
            )
    ) as HashMap<String, Any>

    fun getClickBanner(
            channelId: String,
            userId: String,
            bannerId: String,
            shopId: String,
            creativeName: String,
            widgetPosition: String,
            positionFold: String,
            position: String,
            autoPlay: String,
            channelName: String
    ) : HashMap<String,Any> = getBasicPromotionChannelClick(
            event = Event.PROMO_CLICK,
            eventCategory = HOMEPAGE_CMP,
            eventAction = CLICK,
            eventLabel = EVENT_LABEL_CONTENT_PLAY.format(shopId, channelId, position, positionFold, autoPlay),
            userId = userId,
            channelId = channelId,
            promotions = listOf(
                    Promotion(
                            id = bannerId,
                            name = PROMO_CHANNEL_NAME.format(widgetPosition, channelName),
                            creative = creativeName,
                            position = position
                    )
            )
    ) as HashMap<String, Any>

    fun getClickSeeOtherContent(
            creativeName: String,
            userId: String
    ): HashMap<String, Any>{
        return DataLayer.mapOf(
                Event.KEY, CustomEvent.CLICK_HOMEPAGE,
                Category.KEY, HOMEPAGE_CMP,
                Action.KEY, CLICK_OTHER_CONTENT,
                Label.KEY, creativeName,
                UserId.KEY, userId
        ) as HashMap<String, Any>
    }

    fun getClickSeeAll(userId: String): HashMap<String, Any>{
        return DataLayer.mapOf(
                Event.KEY, CustomEvent.CLICK_HOMEPAGE,
                Category.KEY, HOMEPAGE_CMP,
                Action.KEY, CLICK_VIEW_ALL,
                Label.KEY, EVENT_LABEL_CLICK_VIEW_ALL,
                UserId.KEY, userId
        ) as HashMap<String, Any>
    }

    fun getClickAddRemind(
            channelId: String,
            notifierId: String,
            userId: String
    ): HashMap<String, Any>{
        return DataLayer.mapOf(
                Event.KEY, CustomEvent.CLICK_HOMEPAGE,
                Category.KEY, HOMEPAGE_CMP,
                Action.KEY, CLICK_REMIND_ME,
                Label.KEY, "%s - %s".format(channelId, notifierId),
                UserId.KEY, userId
        ) as HashMap<String, Any>
    }

    fun getClickRemoveRemind(
            channelId: String,
            notifierId: String,
            userId: String
    ): HashMap<String, Any>{
        return DataLayer.mapOf(
                Event.KEY, CustomEvent.CLICK_HOMEPAGE,
                Category.KEY, HOMEPAGE_CMP,
                Action.KEY, CLICK_REMOVE_REMIND_ME,
                Label.KEY, "%s - %s".format(channelId, notifierId),
                UserId.KEY, userId
        ) as HashMap<String, Any>
    }

}