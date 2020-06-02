package com.tokopedia.home.analytics

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.home.analytics.v2.BaseTracking
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
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
        fun getBannerImpression(bannerModel: BannerSlidesModel) = getBasicPromotionView(
                Event.PROMO_VIEW,
                Category.HOMEPAGE,
                Action.IMPRESSION.format(SLIDER_BANNER),
                Label.NONE,
                listOf(
                        Promotion(
                                id= bannerModel.id.toString(),
                                name = PROMO_VALUE,
                                creative = bannerModel.creativeName,
                                position = bannerModel.position.toString(),
                                promoCodes = Label.NONE,
                                promoIds = Label.NONE
                        )
                )
        )

        fun getOverlayBannerImpression(bannerModel: BannerSlidesModel) = getBasicPromotionView(
                Event.PROMO_VIEW,
                Category.HOMEPAGE,
                Action.IMPRESSION.format(OVERLAY_SLIDER_BANNER),
                Label.NONE,
                listOf(
                        Promotion(
                                id= bannerModel.id.toString(),
                                name = PROMO_OVERLAY_VALUE,
                                creative = bannerModel.creativeName,
                                position = bannerModel.position.toString(),
                                promoCodes = Label.NONE,
                                promoIds = Label.NONE
                        )
                )
        )

        fun getBannerClick(bannerModel: BannerSlidesModel) = getBasicPromotionChannelClick(
                event = Event.PROMO_CLICK,
                eventCategory = Category.HOMEPAGE,
                eventAction = Action.CLICK.format(SLIDER_BANNER),
                eventLabel = Label.NONE,
                attribution = bannerModel.galaxyAttribution,
                affinity = bannerModel.persona,
                categoryId = bannerModel.categoryPersona,
                shopId = bannerModel.brandId,
                channelId = Label.NONE,
                campaignCode = bannerModel.campaignCode,
                promotions = listOf(
                        Promotion(
                                id= bannerModel.id.toString(),
                                name = PROMO_VALUE,
                                creative = bannerModel.creativeName,
                                position = bannerModel.position.toString(),
                                promoCodes = Label.NONE,
                                promoIds = Label.NONE
                        )
                )
        )

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
                                id= bannerModel.id.toString(),
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
        fun getLegoBannerFourImageClick(channel: DynamicHomeChannel.Channels, grid: DynamicHomeChannel.Grid, position: Int) = getBasicPromotionChannelClick(
                event = Event.PROMO_CLICK,
                eventCategory = Category.HOMEPAGE,
                eventAction = Action.CLICK.format(LEGO_BANNER_4_IMAGE_NAME),
                eventLabel = grid.attribution,
                channelId = channel.id,
                categoryId = channel.categoryPersona,
                affinity = channel.persona,
                attribution = channel.galaxyAttribution,
                shopId = channel.brandId,
                campaignCode = channel.campaignCode,
                promotions = channel.grids.map {
                    Promotion(
                            id = CustomEvent.FORMAT_4_VALUE_UNDERSCORE.format(channel.id, grid.id, channel.persoType, channel.categoryID),
                            creative = it.attribution,
                            name = channel.promoName,
                            position = position.toString()
                    )
                }
        )

        fun getLegoBannerFourImageSeeAllClick(channel: DynamicHomeChannel.Channels): HashMap<String, Any>{
            return DataLayer.mapOf(
                Event.KEY, CustomEvent.CLICK_HOMEPAGE,
                Category.KEY, Category.HOMEPAGE,
                Action.KEY, Action.CLICK.format(LEGO_BANNER_4_IMAGE_NAME) + " view all",
                Label.KEY, channel.header.name,
                Label.CHANNEL_LABEL, channel.id
            ) as HashMap<String, Any>
        }
    }

    object RecommendationList{
        private const val RECOMMENDATION_LIST_CAROUSEL_PRODUCT = "dynamic channel list carousel"
        private const val RECOMMENDATION_LIST_IMPRESSION_EVENT_ACTION = "impression on dynamic channel list carousel"
        private const val RECOMMENDATION_LIST_CLICK_EVENT_ACTION = "click on dynamic channel list carousel"
        private const val RECOMMENDATION_LIST_CLICK_ADD_TO_CART_EVENT_ACTION = "click add to cart on dynamic channel list carousel"
        private const val RECOMMENDATION_LIST_SEE_ALL_EVENT_ACTION = "click view all card on dynamic channel list carousel"
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

        private fun getRecommendationListClick(channel: DynamicHomeChannel.Channels, grid: DynamicHomeChannel.Grid, position: Int, userId: String) = getBasicProductChannelClick(
                event = Event.PRODUCT_CLICK,
                eventCategory = Category.HOMEPAGE,
                eventAction = RECOMMENDATION_LIST_CLICK_EVENT_ACTION,
                eventLabel = grid.attribution,
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

        fun sendRecommendationListClick(channel: DynamicHomeChannel.Channels, grid: DynamicHomeChannel.Grid, position: Int, userId: String) {
            getTracker().sendEnhanceEcommerceEvent(getRecommendationListClick(channel, grid, position, userId))
        }

        private fun getRecommendationListSeeAllClick(channel: DynamicHomeChannel.Channels): HashMap<String, Any>{
            return DataLayer.mapOf(
                    Event.KEY, Event.CLICK_HOMEPAGE,
                    Category.KEY, Category.HOMEPAGE,
                    Action.KEY, RECOMMENDATION_LIST_SEE_ALL_EVENT_ACTION,
                    Label.KEY, channel.header.name
            ) as HashMap<String, Any>
        }

        private fun getRecommendationListSeeAllCardClick(channel: DynamicHomeChannel.Channels): HashMap<String, Any>{
            return DataLayer.mapOf(
                    Event.KEY, Event.CLICK_HOMEPAGE,
                    Category.KEY, Category.HOMEPAGE,
                    Action.KEY, RECOMMENDATION_LIST_SEE_ALL_CARD_EVENT_ACTION,
                    Label.KEY, channel.header.name
            ) as HashMap<String, Any>
        }

        fun sendRecommendationListSeeAllClick(channel: DynamicHomeChannel.Channels) {
            getTracker().sendGeneralEvent(getRecommendationListSeeAllClick(channel))
        }

        fun sendRecommendationListSeeAllCardClick(channel: DynamicHomeChannel.Channels) {
            getTracker().sendGeneralEvent(getRecommendationListSeeAllCardClick(channel))
        }

        fun getCloseClickOnDynamicListCarousel(channel: DynamicHomeChannel.Channels, userId: String = "") = DataLayer.mapOf(
                Event.KEY, Event.CLICK_HOMEPAGE,
                Category.KEY, Category.HOMEPAGE,
                Action.KEY, RECOMMENDATION_LIST_CLOSE_EVENT_ACTION,
                Label.KEY, channel.header.name,
                Screen.KEY, Screen.DEFAULT,
                UserId.KEY, userId,
                CurrentSite.KEY, CurrentSite.DEFAULT
        )

        fun getAddToCartOnDynamicListCarousel(channel: DynamicHomeChannel.Channels, grid: DynamicHomeChannel.Grid, position: Int, cartId: String, quantity: String = "0", userId: String = "") = DataLayer.mapOf(
                Event.KEY, Event.PRODUCT_ADD_TO_CART,
                Category.KEY, Category.HOMEPAGE,
                Label.KEY, channel.header.name,
                Action.KEY,RECOMMENDATION_LIST_CLICK_ADD_TO_CART_EVENT_ACTION,
                Label.CHANNEL_LABEL, channel.header.name,
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
    }

    object MixLeft {

        private const val LIST_MIX_LEFT = "dynamic channel left carousel"
        private const val IMPRESSION_MIX_LEFT = "impression on product dynamic channel left carousel"
        private const val IMPRESSION_MIX_LEFT_BANNER = "impression on banner dynamic channel left carousel"
        private const val CLICK_MIX_LEFT_BANNER = "click on banner dynamic channel left carousel"
        private const val CLICK_MIX_LEFT = "click on product dynamic channel left carousel"
        private const val PROMOTION_BANNER_ID = "%s_%s_%s_%s"
        private const val PROMOTION_BANNER_NAME = "'/ - p%s - dynamic channel left carousel - banner - %s"


        private const val CLICK_MIX_LEFT_LOADMORE = "click view all on dynamic channel left carousel"
        private const val CLICK_MIX_LEFT_LOADMORE_CARD = "click view all card on dynamic channel left carousel"
        fun getMixLeftClickLoadMore(channel: DynamicHomeChannel.Channels): HashMap<String, Any> {
            return DataLayer.mapOf(
                    Event.KEY, CustomEvent.CLICK_HOMEPAGE,
                    Category.KEY, Category.HOMEPAGE,
                    Action.KEY, CLICK_MIX_LEFT_LOADMORE,
                    Label.KEY, channel.id + " - " + channel.header.name
            ) as HashMap<String, Any>
        }

        fun getMixLeftClickLoadMoreCard(channel: DynamicHomeChannel.Channels, userId: String): HashMap<String, Any> {
            return DataLayer.mapOf(
                    Event.KEY, CustomEvent.CLICK_HOMEPAGE,
                    Category.KEY, Category.HOMEPAGE,
                    Action.KEY, CLICK_MIX_LEFT_LOADMORE_CARD,
                    Label.KEY, channel.id + " - " + channel.header.name,
                    CurrentSite.KEY, CurrentSite.DEFAULT,
                    Screen.KEY, Screen.DEFAULT,
                    UserId.KEY, userId,
                    BusinessUnit.KEY, BusinessUnit.DEFAULT
            ) as HashMap<String, Any>
        }

        fun getMixLeftProductView(channel: DynamicHomeChannel.Channels, grid: DynamicHomeChannel.Grid, position:Int) = getBasicProductChannelView(
                event = Event.PRODUCT_VIEW,
                eventCategory = Category.HOMEPAGE,
                eventAction = IMPRESSION_MIX_LEFT,
                eventLabel = channel.header.name,
                products =  listOf(Product(
                        name = grid.name,
                        id = grid.id,
                        productPrice = convertRupiahToInt(
                                grid.price
                        ).toString(),
                        brand = Value.NONE_OTHER,
                        category = Value.NONE_OTHER,
                        variant = Value.NONE_OTHER,
                        productPosition = (position + 1).toString(),
                        channelId = channel.id,
                        isFreeOngkir = grid.freeOngkir.isActive,
                        persoType = channel.persoType,
                        categoryId = channel.categoryID,
                        isTopAds = grid.isTopads
                )),
                list = String.format(
                        Value.LIST_WITH_HEADER, "1", LIST_MIX_LEFT, channel.header.name
                ),
                channelId = channel.id
        )

        fun getMixLeftIrisProductView(channel: DynamicHomeChannel.Channels) = getBasicProductChannelView(
                event = Event.PRODUCT_VIEW_IRIS,
                eventCategory = Category.HOMEPAGE,
                eventAction = IMPRESSION_MIX_LEFT,
                eventLabel = channel.header.name,
                products = channel.grids.mapIndexed { index, grid ->
                    Product(
                            name = grid.name,
                            id = grid.id,
                            productPrice = convertRupiahToInt(
                                    grid.price
                            ).toString(),
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
                        Value.LIST_WITH_HEADER, "1", LIST_MIX_LEFT, channel.header.name
                ),
                channelId = channel.id
        )

        fun getMixLeftProductClick(channel: DynamicHomeChannel.Channels, grid: DynamicHomeChannel.Grid, position: Int) = getBasicProductChannelClick(
                event = Event.PRODUCT_CLICK,
                eventCategory = Category.HOMEPAGE,
                eventAction = CLICK_MIX_LEFT,
                eventLabel = channel.header.name,
                channelId = channel.id,
                campaignCode = channel.campaignCode,
                products = listOf(
                        Product(
                                name = grid.name,
                                id = grid.id,
                                productPrice = convertRupiahToInt(
                                        grid.price
                                ).toString(),
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
                        Value.LIST_WITH_HEADER, "1", LIST_MIX_LEFT, channel.header.name
                )
        )

        fun getMixLeftBannerView(channel: DynamicHomeChannel.Channels, position: Int) = getBasicPromotionView(
                event = Event.PROMO_VIEW,
                eventCategory = Category.HOMEPAGE,
                eventAction = IMPRESSION_MIX_LEFT_BANNER,
                eventLabel = Label.NONE,
                promotions = listOf(
                        Promotion(
                                id = CustomEvent.FORMAT_4_VALUE_UNDERSCORE.format(channel.id, channel.banner.id, channel.banner.attribution, channel.categoryPersona),
                                creative = channel.banner.attribution,
                                name = PROMOTION_BANNER_NAME.format("1", channel.header.name),
                                position = position.toString()
                        )
                )
        )

        fun getMixLeftBannerClick(channel: DynamicHomeChannel.Channels, position: Int) = getBasicPromotionChannelClick(
                event = Event.PROMO_CLICK,
                eventCategory = Category.HOMEPAGE,
                eventAction = CLICK_MIX_LEFT_BANNER,
                campaignCode = channel.campaignCode,
                eventLabel = channel.id + " - " + channel.header.name,
                channelId = channel.id,
                categoryId = channel.categoryPersona,
                affinity = channel.persona,
                attribution = channel.galaxyAttribution,
                shopId = channel.brandId,
                promotions = listOf(
                        Promotion(
                                id = CustomEvent.FORMAT_4_VALUE_UNDERSCORE.format(channel.id, channel.banner.id, channel.banner.attribution, channel.categoryPersona),
                                creative = channel.banner.attribution,
                                name = PROMOTION_BANNER_NAME.format("1", channel.header.name),
                                position = position.toString()
                        )
                )
        )

        fun sendMixLeftProductClick(channel: DynamicHomeChannel.Channels, grid: DynamicHomeChannel.Grid, position: Int) {
            getTracker().sendEnhanceEcommerceEvent(getMixLeftProductClick(channel, grid, position))
        }

        fun sendMixLeftSeeAllCardClick(channel: DynamicHomeChannel.Channels, userId: String){
            getTracker().sendEnhanceEcommerceEvent(getMixLeftClickLoadMoreCard(channel, userId))
        }

        fun sendMixLeftSeeAllClick(channel: DynamicHomeChannel.Channels, userId: String) {
            getTracker().sendEnhanceEcommerceEvent(getMixLeftClickLoadMore(channel))
        }

        fun sendMixLeftBannerClick(channel: DynamicHomeChannel.Channels, position: Int){
                getTracker().sendEnhanceEcommerceEvent(getMixLeftBannerClick(channel, position))
        }
    }

    object PopularKeyword {
        private const val CLICK_POPULAR_KEYWORDS = "click on popular keyword banner"
        private const val CLICK_POPULAR_KEYWORDS_RELOAD = "click view all on popular keyword banner"
        private const val IMPRESSION_POPULAR_KEYWORDS = "impression on popular keyword banner"
        private const val POPULAR_KEYWORDS_NAME = "popular keyword banner"
        fun getPopularKeywordImpressionItem(channel: DynamicHomeChannel.Channels, position: Int, keyword: String) = getBasicPromotionView(
                event = Event.PROMO_VIEW,
                eventCategory = Category.HOMEPAGE,
                eventAction = IMPRESSION_POPULAR_KEYWORDS,
                eventLabel = String.format(BaseTracking.Label.FORMAT_2_ITEMS, channel.header.name, keyword),
                promotions = channel.grids.map {
                    Promotion(
                            id = channel.id,
                            creative = it.attribution,
                            name = Ecommerce.PROMOTION_NAME.format(position, POPULAR_KEYWORDS_NAME, keyword),
                            position = position.toString()
                    )

                })

        fun getPopularKeywordImpressionIrisItem(channel: DynamicHomeChannel.Channels, position: Int, keyword: String) = getBasicPromotionChannelView(
                event = Event.PROMO_VIEW_IRIS,
                eventCategory = Category.HOMEPAGE,
                eventAction = IMPRESSION_POPULAR_KEYWORDS,
                eventLabel = String.format(BaseTracking.Label.FORMAT_2_ITEMS, channel.header.name, keyword),
                channelId = channel.id,
                promotions = channel.grids.map {
                    Promotion(
                            id = channel.id,
                            creative = it.attribution,
                            name = Ecommerce.PROMOTION_NAME.format(position, POPULAR_KEYWORDS_NAME, keyword),
                            position = position.toString()
                    )

                })

        fun getPopularKeywordClickItem(channel: DynamicHomeChannel.Channels, position: Int, keyword: String) = getBasicPromotionChannelClick(
                event = Event.PROMO_CLICK,
                eventCategory = Category.HOMEPAGE,
                eventAction = CLICK_POPULAR_KEYWORDS,
                eventLabel = channel.header.name,
                channelId = channel.id,
                categoryId = channel.categoryPersona,
                affinity = channel.persona,
                attribution = channel.galaxyAttribution,
                shopId = channel.brandId,
                campaignCode = channel.campaignCode,
                promotions = channel.grids.map {
                    Promotion(
                            id = channel.id,
                            creative = it.attribution,
                            name = Ecommerce.PROMOTION_NAME.format(position, POPULAR_KEYWORDS_NAME, keyword),
                            position = position.toString()
                    )

                })

        fun sendPopularKeywordClickItem(channel: DynamicHomeChannel.Channels, position: Int, keyword: String) {
            getTracker().sendEnhanceEcommerceEvent(getPopularKeywordClickItem(channel, position, keyword))
        }

        fun getPopularKeywordClickReload(channel: DynamicHomeChannel.Channels): HashMap<String, Any> {
            return DataLayer.mapOf(
                    Event.KEY, CustomEvent.CLICK_HOMEPAGE,
                    Category.KEY, Category.HOMEPAGE,
                    Action.KEY, CLICK_POPULAR_KEYWORDS_RELOAD,
                    Label.KEY, channel.header.name,
                    ChannelId.KEY, channel.id
            ) as HashMap<String, Any>
        }

        fun sendPopularKeywordClickReload(channel: DynamicHomeChannel.Channels) {
            getTracker().sendGeneralEvent(getPopularKeywordClickReload(channel))
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
