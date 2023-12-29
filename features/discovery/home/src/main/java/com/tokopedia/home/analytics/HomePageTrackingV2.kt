package com.tokopedia.home.analytics

import com.tokopedia.home.analytics.v2.BaseTracking
import com.tokopedia.home.analytics.v2.BaseTrackingBuilder
import com.tokopedia.home.beranda.domain.model.banner.BannerSlidesModel

object HomePageTrackingV2 : BaseTracking() {
    private object CustomEvent{
        const val CLICK_HOMEPAGE = "clickHomepage"
        const val FORMAT_4_VALUE_UNDERSCORE = "%s_%s_%s_%s";
    }

    object HomeBanner{
        private const val SLIDER_BANNER = "slider banner"
        private const val OVERLAY_SLIDER_BANNER = "overlay slider banner"
        private const val PROMO_VALUE = "/ - p1 - promo"
        private const val PROMO_OVERLAY_VALUE = "/ - p1 - promo overlay"

        fun getBannerImpression(bannerModel: BannerSlidesModel): Map<String, Any>  {
            val trackingBuilder = BaseTrackingBuilder()
            return trackingBuilder.constructBasicPromotionView(
                    event = Event.PROMO_VIEW,
                    eventCategory = Category.HOMEPAGE,
                    eventAction = Action.IMPRESSION.format(SLIDER_BANNER),
                    eventLabel =  Label.NONE,
                    promotions = listOf(
                            Promotion(
                                    id= CustomEvent.FORMAT_4_VALUE_UNDERSCORE.format("0", bannerModel.id, "0", bannerModel.categoryId),
                                    name = PROMO_VALUE,
                                    creative = bannerModel.creativeName,
                                    position = bannerModel.position.toString(),
                                    promoCodes = Label.NONE,
                                    promoIds = Label.NONE
                            )
                    ))
                    .build()
        }

        fun getOverlayBannerImpression(bannerModel: BannerSlidesModel) = getBasicPromotionView(
                Event.PROMO_VIEW,
                Category.HOMEPAGE,
                Action.IMPRESSION.format(OVERLAY_SLIDER_BANNER),
                Label.NONE,
                listOf(
                        Promotion(
                                id= CustomEvent.FORMAT_4_VALUE_UNDERSCORE.format("0", bannerModel.id, "0", bannerModel.categoryId),
                                name = PROMO_OVERLAY_VALUE,
                                creative = bannerModel.creativeName,
                                position = bannerModel.position.toString(),
                                promoCodes = Label.NONE,
                                promoIds = Label.NONE
                        )
                )
        )

        fun getBannerClick(bannerModel: BannerSlidesModel): Map<String, Any> {
            val trackingBuilder = BaseTrackingBuilder()
            return trackingBuilder.constructBasicPromotionClick(
                    event = Event.PROMO_CLICK,
                    eventCategory = Category.HOMEPAGE,
                    eventAction = Action.CLICK.format(SLIDER_BANNER),
                    eventLabel = Label.NONE,
                    promotions = listOf(
                            Promotion(
                                    id= CustomEvent.FORMAT_4_VALUE_UNDERSCORE.format("0", bannerModel.id, "0", bannerModel.categoryId),
                                    name = PROMO_VALUE,
                                    creative = bannerModel.creativeName,
                                    position = bannerModel.position.toString(),
                                    promoCodes = Label.NONE,
                                    promoIds = Label.NONE
                            )
                    ))
                    .appendAttribution(bannerModel.galaxyAttribution)
                    .appendAffinity(bannerModel.persona)
                    .appendCategoryId(bannerModel.categoryPersona)
                    .appendShopId(bannerModel.brandId)
                    .appendChannelId(Label.NONE)
                    .appendCampaignCode(bannerModel.campaignCode)
                    .build()
        }

        fun getOverlayBannerClick(bannerModel: BannerSlidesModel) = getBasicPromotionChannelClick(
                event = Event.PROMO_CLICK,
                eventCategory = Category.HOMEPAGE,
                eventAction = Action.CLICK.format(OVERLAY_SLIDER_BANNER),
                eventLabel = Label.NONE,
                attribution = bannerModel.galaxyAttribution,
                affinity = bannerModel.persona,
                categoryId = bannerModel.categoryPersona,
                shopId = bannerModel.brandId,
                channelId = Label.NONE,
                campaignCode = bannerModel.campaignCode,
                promotions = listOf(
                        Promotion(
                                id= CustomEvent.FORMAT_4_VALUE_UNDERSCORE.format("0", bannerModel.id, "0", bannerModel.categoryId),
                                name = PROMO_OVERLAY_VALUE,
                                creative = bannerModel.creativeName,
                                position = bannerModel.position.toString(),
                                promoCodes = Label.NONE,
                                promoIds = Label.NONE
                        )
                )
        )
    }
}
