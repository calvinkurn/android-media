package com.tokopedia.home.analytics

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.home.analytics.v2.BaseTracking
import com.tokopedia.home.analytics.v2.BaseTrackingBuilder
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.domain.model.banner.BannerSlidesModel
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel

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

    object LegoBanner{
        private const val LEGO_BANNER_4_IMAGE_NAME = "lego banner 4 image"
        private const val LEGO_BANNER_3_IMAGE_NAME = "lego banner 3 image"
        private const val LEGO_BANNER_6_IMAGE_NAME = "lego banner"

        fun getLegoBannerFourImageImpression(channel: DynamicHomeChannel.Channels, position: Int, isToIris: Boolean = false) = getBasicPromotionChannelView(
                event = if(isToIris) Event.PROMO_VIEW_IRIS else Event.PROMO_VIEW,
                eventCategory = Category.HOMEPAGE,
                eventAction = Action.IMPRESSION.format(LEGO_BANNER_4_IMAGE_NAME),
                eventLabel = Label.NONE,
                promotions = channel.grids.mapIndexed { index, grid ->
                    Promotion(
                            id = CustomEvent.FORMAT_4_VALUE_UNDERSCORE.format(channel.id, grid.id, channel.persoType, channel.categoryID),
                            creative = grid.attribution,
                            name = Ecommerce.PROMOTION_NAME.format(position, LEGO_BANNER_4_IMAGE_NAME, channel.header.name),
                            position = (index + 1).toString()
                    )
                },
                channelId = channel.id
        )

        fun getLegoBannerFourImageImpression(channel: ChannelModel, position: Int, isToIris: Boolean = false) = getBasicPromotionChannelView(
                event = if(isToIris) Event.PROMO_VIEW_IRIS else Event.PROMO_VIEW,
                eventCategory = Category.HOMEPAGE,
                eventAction = Action.IMPRESSION.format(LEGO_BANNER_4_IMAGE_NAME),
                eventLabel = Label.NONE,
                promotions = channel.channelGrids.mapIndexed { index, grid ->
                    Promotion(
                            id = CustomEvent.FORMAT_4_VALUE_UNDERSCORE.format(channel.id, grid.id, channel.trackingAttributionModel.persoType, channel.trackingAttributionModel.categoryId),
                            creative = grid.attribution,
                            name = Ecommerce.PROMOTION_NAME.format(position, LEGO_BANNER_4_IMAGE_NAME, channel.channelHeader.name),
                            position = (index + 1).toString()
                    )
                },
                channelId = channel.id
        )

        fun getLegoBannerThreeImageImpression(channel: ChannelModel, position: Int, isToIris: Boolean = false) = getBasicPromotionChannelView(
                event = if(isToIris) Event.PROMO_VIEW_IRIS else Event.PROMO_VIEW,
                eventCategory = Category.HOMEPAGE,
                eventAction = Action.IMPRESSION.format(LEGO_BANNER_3_IMAGE_NAME),
                eventLabel = Label.NONE,
                promotions = channel.channelGrids.mapIndexed { index, grid ->
                    Promotion(
                            id = CustomEvent.FORMAT_4_VALUE_UNDERSCORE.format(channel.id, grid.id, channel.trackingAttributionModel.persoType, channel.trackingAttributionModel.categoryId),
                            creative = grid.attribution,
                            name = Ecommerce.PROMOTION_NAME.format(position, LEGO_BANNER_3_IMAGE_NAME, channel.channelHeader.name),
                            position = (index + 1).toString()
                    )
                },
                channelId = channel.id
        )

        fun getLegoBannerSixImageImpression(channel: ChannelModel, position: Int, isToIris: Boolean = false) = getBasicPromotionChannelView(
                event = if(isToIris) Event.PROMO_VIEW_IRIS else Event.PROMO_VIEW,
                eventCategory = Category.HOMEPAGE,
                eventAction = Action.IMPRESSION.format(LEGO_BANNER_6_IMAGE_NAME),
                eventLabel = Label.NONE,
                promotions = channel.channelGrids.mapIndexed { index, grid ->
                    Promotion(
                            id = CustomEvent.FORMAT_4_VALUE_UNDERSCORE.format(channel.id, grid.id, channel.trackingAttributionModel.persoType, channel.trackingAttributionModel.categoryId),
                            creative = grid.attribution,
                            name = Ecommerce.PROMOTION_NAME.format(position, LEGO_BANNER_6_IMAGE_NAME, channel.channelHeader.name),
                            position = (index + 1).toString()
                    )
                },
                channelId = channel.id
        )

        fun getLegoBannerFourImageClick(channel: ChannelModel, grid: ChannelGrid, position: Int) = getBasicPromotionChannelClick(
                event = Event.PROMO_CLICK,
                eventCategory = Category.HOMEPAGE,
                eventAction = Action.CLICK.format(LEGO_BANNER_4_IMAGE_NAME),
                eventLabel = grid.attribution,
                channelId = channel.id,
                categoryId = channel.trackingAttributionModel.categoryPersona,
                affinity = channel.trackingAttributionModel.persona,
                attribution = channel.trackingAttributionModel.galaxyAttribution,
                shopId = channel.trackingAttributionModel.brandId,
                campaignCode = channel.trackingAttributionModel.campaignCode,
                promotions = listOf(
                        Promotion(
                                id = CustomEvent.FORMAT_4_VALUE_UNDERSCORE.format(channel.id, grid.id, channel.trackingAttributionModel.persoType, channel.trackingAttributionModel.categoryId),
                                creative = grid.attribution,
                                name = channel.trackingAttributionModel.promoName,
                                position = position.toString()
                        )
                )
        )

        fun getLegoBannerFourImageSeeAllClick(channelHeaderName: String, channelId: String): HashMap<String, Any>{
            return DataLayer.mapOf(
                Event.KEY, CustomEvent.CLICK_HOMEPAGE,
                Category.KEY, Category.HOMEPAGE,
                Action.KEY, Action.CLICK.format(LEGO_BANNER_4_IMAGE_NAME) + " view all",
                Label.KEY, "$channelId - $channelHeaderName",
                Label.CHANNEL_LABEL, channelId
            ) as HashMap<String, Any>
        }
    }


    object SprintSale{
        private const val EVENT_ACTION_SPRINT_SALE_IMPRESSION = "sprint sale impression"
        private const val EVENT_ACTION_SPRINT_SALE_CLICK = "sprint sale click"
        private const val EVENT_ACTION_SPRINT_SALE_CLICK_VIEW_ALL = "sprint sale click view all"
        private const val LIST_VALUE_SPRINT_SALE = "sprint sale"

        fun getSprintSaleImpression(channel: DynamicHomeChannel.Channels, isToIris: Boolean = false) = getBasicProductView(
                event = if(isToIris) Event.PRODUCT_VIEW_IRIS else Event.PRODUCT_VIEW,
                eventCategory = Category.HOMEPAGE,
                eventAction = EVENT_ACTION_SPRINT_SALE_IMPRESSION,
                eventLabel = Label.NONE,
                products = channel.grids.mapIndexed { index, grid ->
                    Product(
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
                            isTopAds = grid.isTopads
                    )
                },
                list = String.format(
                        Value.LIST_WITH_HEADER, "1", LIST_VALUE_SPRINT_SALE, channel.header.name
                )
        )
        private fun getSprintSaleClick(channel: DynamicHomeChannel.Channels, currentCountDown: String, grid: DynamicHomeChannel.Grid, position: Int) = getBasicProductChannelClick(
                event = Event.PRODUCT_CLICK,
                eventCategory = Category.HOMEPAGE,
                eventAction = EVENT_ACTION_SPRINT_SALE_CLICK,
                eventLabel = currentCountDown,
                channelId = channel.id,
                campaignCode = channel.campaignCode,
                products = listOf(
                        Product(
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
                                isTopAds = grid.isTopads
                        )
                ),
                list = String.format(
                        Value.LIST_WITH_HEADER, "1", LIST_VALUE_SPRINT_SALE, channel.header.name
                )
        )

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
