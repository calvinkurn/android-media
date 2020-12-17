package com.tokopedia.home.analytics

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.home.analytics.v2.BaseTracking
import com.tokopedia.home.analytics.v2.BaseTrackingBuilder
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.domain.model.banner.BannerSlidesModel
import com.tokopedia.track.builder.BaseTrackerBuilder
import com.tokopedia.track.builder.util.BaseTrackerConst

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

    object SprintSale{
        private const val EVENT_ACTION_SPRINT_SALE_IMPRESSION = "sprint sale impression"
        private const val EVENT_ACTION_SPRINT_SALE_CLICK = "sprint sale click"
        private const val EVENT_ACTION_SPRINT_SALE_CLICK_VIEW_ALL = "sprint sale click view all"
        private const val LIST_VALUE_SPRINT_SALE = "sprint sale"

        fun getSprintSaleImpression(channel: DynamicHomeChannel.Channels, isToIris: Boolean = false) = BaseTrackerBuilder().constructBasicProductView(
                event = if(isToIris) Event.PRODUCT_VIEW_IRIS else Event.PRODUCT_VIEW,
                eventCategory = Category.HOMEPAGE,
                eventAction = EVENT_ACTION_SPRINT_SALE_IMPRESSION,
                eventLabel = Label.NONE,
                products = channel.grids.mapIndexed { index, grid ->
                    BaseTrackerConst.Product(
                            name = grid.name,
                            id =  grid.id,
                            productPrice = convertRupiahToInt(grid.price).toString(),
                            brand = Value.NONE_OTHER,
                            category = Value.NONE_OTHER,
                            variant = Value.NONE_OTHER,
                            productPosition = (index + 1).toString(),
                            channelId = channel.id,
                            isFreeOngkir = grid.freeOngkir.isActive,
                            persoType = channel.persoType,
                            categoryId = channel.categoryID,
                            recommendationType = grid.recommendationType,
                            pageName = channel.pageName,
                            isTopAds = grid.isTopads,
                            isCarousel = false,
                            headerName = channel.header.name
                    )
                },
                list = String.format(
                        Value.LIST, "1", LIST_VALUE_SPRINT_SALE
                )
        ).build()

        private fun getSprintSaleClick(channel: DynamicHomeChannel.Channels, currentCountDown: String, grid: DynamicHomeChannel.Grid, position: Int) = BaseTrackerBuilder().constructBasicProductClick(
                event = Event.PRODUCT_CLICK,
                eventCategory = Category.HOMEPAGE,
                eventAction = EVENT_ACTION_SPRINT_SALE_CLICK,
                eventLabel = currentCountDown,
                products = listOf(
                        BaseTrackerConst.Product(
                                name = grid.name,
                                id = grid.id,
                                productPrice = convertRupiahToInt(grid.price).toString(),
                                brand = Value.NONE_OTHER,
                                category = Value.NONE_OTHER,
                                variant = Value.NONE_OTHER,
                                productPosition = (position + 1).toString(),
                                channelId = channel.id,
                                isFreeOngkir = grid.freeOngkir.isActive,
                                persoType = channel.persoType,
                                categoryId = channel.categoryID,
                                recommendationType = grid.recommendationType,
                                pageName = channel.pageName,
                                isTopAds = grid.isTopads
                        )
                ),
                list = String.format(
                        Value.LIST, "1", LIST_VALUE_SPRINT_SALE
                )
        ).appendChannelId(channel.id)
        .appendCampaignCode(channel.campaignCode)
        .build()

        fun sendSprintSaleClick(channel: DynamicHomeChannel.Channels, currentCountDown: String, grid: DynamicHomeChannel.Grid, position: Int) {
            getTracker().sendEnhanceEcommerceEvent(getSprintSaleClick(channel, currentCountDown, grid, position))
        }

        private fun getSprintSaleSeeAllClick(channel: DynamicHomeChannel.Channels): HashMap<String, Any>{
            return DataLayer.mapOf(
                    Event.KEY, Event.CLICK_HOMEPAGE,
                    Category.KEY, Category.HOMEPAGE,
                    Action.KEY, EVENT_ACTION_SPRINT_SALE_CLICK_VIEW_ALL,
                    Label.KEY, channel.header.name,
                    ChannelId.KEY, channel.id
            ) as HashMap<String, Any>
        }

        fun sendSprintSaleSeeAllClick(channel: DynamicHomeChannel.Channels) {
            getTracker().sendGeneralEvent(getSprintSaleSeeAllClick(channel))
        }
    }

}
