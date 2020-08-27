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
            return trackingBuilder.constructBasicPromotionView(
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

    object RecommendationList{
        private const val RECOMMENDATION_LIST_CAROUSEL_PRODUCT = "dynamic channel list carousel"
        private const val RECOMMENDATION_LIST_IMPRESSION_EVENT_ACTION = "impression on dynamic channel list carousel"
        private const val RECOMMENDATION_LIST_CLICK_EVENT_ACTION = "click on dynamic channel list carousel"
        private const val RECOMMENDATION_LIST_CLICK_ADD_TO_CART_EVENT_ACTION = "click add to cart on dynamic channel list carousel"
        private const val RECOMMENDATION_LIST_SEE_ALL_EVENT_ACTION = "click view all on dynamic channel list carousel"
        private const val RECOMMENDATION_LIST_SEE_ALL_CARD_EVENT_ACTION = "click view all card on dynamic channel list carousel"
        private const val RECOMMENDATION_LIST_CLOSE_EVENT_ACTION = "click on close dynamic channel list carousel"

        fun getRecommendationListImpression(channel: DynamicHomeChannel.Channels, isToIris: Boolean = false, userId: String) = getBasicProductChannelView(
                event = if(isToIris) Event.PRODUCT_VIEW_IRIS else Event.PRODUCT_VIEW,
                eventCategory = Category.HOMEPAGE,
                eventAction = RECOMMENDATION_LIST_IMPRESSION_EVENT_ACTION,
                eventLabel = Label.NONE,
                userId = userId,
                products = channel.grids.mapIndexed { index, grid ->
                    Product(
                            name = grid.name,
                            id = grid.id,
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
                        Value.LIST_WITH_HEADER, "1", RECOMMENDATION_LIST_CAROUSEL_PRODUCT, channel.header.name
                ),
                channelId = channel.id
        )

        fun getRecommendationListImpression(channel: ChannelModel, isToIris: Boolean = false, userId: String) = getBasicProductChannelView(
                event = if(isToIris) Event.PRODUCT_VIEW_IRIS else Event.PRODUCT_VIEW,
                eventCategory = Category.HOMEPAGE,
                eventAction = RECOMMENDATION_LIST_IMPRESSION_EVENT_ACTION,
                eventLabel = Label.NONE,
                userId = userId,
                products = channel.channelGrids.mapIndexed { index, grid ->
                    Product(
                            name = grid.name,
                            id = grid.id,
                            productPrice = convertRupiahToInt(grid.price).toString(),
                            brand = Value.NONE_OTHER,
                            category = Value.NONE_OTHER,
                            variant = Value.NONE_OTHER,
                            productPosition = (index + 1).toString(),
                            channelId = channel.id,
                            isFreeOngkir = grid.isFreeOngkirActive,
                            persoType = channel.trackingAttributionModel.persoType,
                            categoryId = channel.trackingAttributionModel.categoryId,
                            isTopAds = grid.isTopads
                    )
                },
                list = String.format(
                        Value.LIST_WITH_HEADER, "1", RECOMMENDATION_LIST_CAROUSEL_PRODUCT, channel.channelHeader.name
                ),
                channelId = channel.id
        )

        private fun getRecommendationListClick(channel: DynamicHomeChannel.Channels, grid: DynamicHomeChannel.Grid, position: Int, userId: String) = getBasicProductChannelClick(
                event = Event.PRODUCT_CLICK,
                eventCategory = Category.HOMEPAGE,
                eventAction = RECOMMENDATION_LIST_CLICK_EVENT_ACTION,
                eventLabel = channel.id +" - "+ channel.header.name,
                channelId = channel.id,
                campaignCode = channel.campaignCode,
                userId = userId,
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
                        Value.LIST_WITH_HEADER, "1", RECOMMENDATION_LIST_CAROUSEL_PRODUCT, channel.header.name
                )
        )

        private fun getRecommendationListClickHomeComponent(channel: ChannelModel, grid: ChannelGrid, position: Int, userId: String) = getBasicProductChannelClick(
                event = Event.PRODUCT_CLICK,
                eventCategory = Category.HOMEPAGE,
                eventAction = RECOMMENDATION_LIST_CLICK_EVENT_ACTION,
                eventLabel = channel.id +" - "+ channel.channelHeader.name,
                channelId = channel.id,
                campaignCode = channel.trackingAttributionModel.campaignCode,
                userId = userId,
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
                                isFreeOngkir = grid.isFreeOngkirActive,
                                persoType = channel.trackingAttributionModel.persoType,
                                categoryId = channel.trackingAttributionModel.categoryId,
                                isTopAds = grid.isTopads
                        )
                ),
                list = String.format(
                        Value.LIST_WITH_HEADER, "1", RECOMMENDATION_LIST_CAROUSEL_PRODUCT, channel.channelHeader.name
                )
        )

        fun sendRecommendationListClick(channel: DynamicHomeChannel.Channels, grid: DynamicHomeChannel.Grid, position: Int, userId: String) {
            getTracker().sendEnhanceEcommerceEvent(getRecommendationListClick(channel, grid, position, userId))
        }

        fun sendRecommendationListHomeComponentClick(channel: ChannelModel, grid: ChannelGrid, position: Int, userId: String) {
            getTracker().sendEnhanceEcommerceEvent(getRecommendationListClickHomeComponent(channel, grid, position, userId))
        }

        private fun getRecommendationListSeeAllClick(channelId: String, headerName: String, userId: String): HashMap<String, Any>{
            return DataLayer.mapOf(
                    Event.KEY, Event.CLICK_HOMEPAGE,
                    Category.KEY, Category.HOMEPAGE,
                    Action.KEY, RECOMMENDATION_LIST_SEE_ALL_EVENT_ACTION,
                    Label.KEY, Label.FORMAT_2_ITEMS.format(channelId, headerName),
                    UserId.KEY, userId
            ) as HashMap<String, Any>
        }

        private fun getRecommendationListSeeAllCardClick(channel: DynamicHomeChannel.Channels, userId: String): HashMap<String, Any>{
            return DataLayer.mapOf(
                    Event.KEY, Event.CLICK_HOMEPAGE,
                    Category.KEY, Category.HOMEPAGE,
                    Action.KEY, RECOMMENDATION_LIST_SEE_ALL_CARD_EVENT_ACTION,
                    Label.KEY, Label.FORMAT_2_ITEMS.format(channel.id, channel.header.name),
                    UserId.KEY, userId
            ) as HashMap<String, Any>
        }

        fun sendRecommendationListSeeAllClick(channelId: String, headerName: String, userId: String) {
            getTracker().sendGeneralEvent(getRecommendationListSeeAllClick(channelId, headerName, userId))
        }

        fun sendRecommendationListSeeAllCardClick(channel: DynamicHomeChannel.Channels, userId: String) {
            getTracker().sendGeneralEvent(getRecommendationListSeeAllCardClick(channel, userId))
        }

        fun getCloseClickOnDynamicListCarousel(channel: DynamicHomeChannel.Channels, userId: String = "") = DataLayer.mapOf(
                Event.KEY, Event.CLICK_HOMEPAGE,
                Category.KEY, Category.HOMEPAGE,
                Action.KEY, RECOMMENDATION_LIST_CLOSE_EVENT_ACTION,
                Label.KEY, Label.FORMAT_2_ITEMS.format(channel.id, channel.header.name),
                Screen.KEY, Screen.DEFAULT,
                UserId.KEY, userId,
                CurrentSite.KEY, CurrentSite.DEFAULT
        )

        fun getCloseClickOnDynamicListCarouselHomeComponent(channel: ChannelModel, userId: String = "") = DataLayer.mapOf(
                Event.KEY, Event.CLICK_HOMEPAGE,
                Category.KEY, Category.HOMEPAGE,
                Action.KEY, RECOMMENDATION_LIST_CLOSE_EVENT_ACTION,
                Label.KEY, Label.FORMAT_2_ITEMS.format(channel.id, channel.channelHeader.name),
                Screen.KEY, Screen.DEFAULT,
                UserId.KEY, userId,
                CurrentSite.KEY, CurrentSite.DEFAULT
        )

        fun getAddToCartOnDynamicListCarousel(channel: DynamicHomeChannel.Channels, grid: DynamicHomeChannel.Grid, position: Int, cartId: String, quantity: String = "0", userId: String = "") = DataLayer.mapOf(
                Event.KEY, Event.PRODUCT_ADD_TO_CART,
                Category.KEY, Category.HOMEPAGE,
                Label.KEY, Label.FORMAT_2_ITEMS.format(channel.id, channel.header.name),
                Action.KEY,RECOMMENDATION_LIST_CLICK_ADD_TO_CART_EVENT_ACTION,
                Label.CAMPAIGN_CODE, channel.campaignCode,
                Screen.KEY, Screen.DEFAULT,
                CurrentSite.KEY, CurrentSite.DEFAULT,
                UserId.KEY, userId,
                Label.CHANNEL_LABEL, channel.id,
                Ecommerce.KEY, Ecommerce.getEcommerceProductAddToCart(
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
                                    isTopAds = grid.isTopads,
                                    quantity = quantity,
                                    cartId = cartId
                            )
                    ),
                    list = String.format(
                            Value.LIST_WITH_HEADER, "1", RECOMMENDATION_LIST_CAROUSEL_PRODUCT, channel.header.name
                    )
                )

        )

        fun getAddToCartOnDynamicListCarouselHomeComponent(channel: ChannelModel, grid: ChannelGrid, position: Int, cartId: String, quantity: String = "0", userId: String = "") = DataLayer.mapOf(
                Event.KEY, Event.PRODUCT_ADD_TO_CART,
                Category.KEY, Category.HOMEPAGE,
                Label.KEY, Label.FORMAT_2_ITEMS.format(channel.id, channel.channelHeader.name),
                Action.KEY,RECOMMENDATION_LIST_CLICK_ADD_TO_CART_EVENT_ACTION,
                Label.CAMPAIGN_CODE, channel.trackingAttributionModel.campaignCode,
                Screen.KEY, Screen.DEFAULT,
                CurrentSite.KEY, CurrentSite.DEFAULT,
                UserId.KEY, userId,
                Label.CHANNEL_LABEL, channel.id,
                Ecommerce.KEY, Ecommerce.getEcommerceProductAddToCart(
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
                                isFreeOngkir = grid.isFreeOngkirActive,
                                persoType = channel.trackingAttributionModel.persoType,
                                categoryId = channel.trackingAttributionModel.categoryId,
                                isTopAds = grid.isTopads,
                                quantity = quantity,
                                cartId = cartId
                        )
                ),
                list = String.format(
                        Value.LIST_WITH_HEADER, "1", RECOMMENDATION_LIST_CAROUSEL_PRODUCT, channel.channelHeader.name
                )
        )

        )
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
