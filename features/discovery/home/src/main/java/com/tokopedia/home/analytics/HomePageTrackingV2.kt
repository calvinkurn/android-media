package com.tokopedia.home.analytics

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.design.utils.CurrencyFormatHelper
import com.tokopedia.home.analytics.v2.BaseTracking
import com.tokopedia.home.analytics.v2.BaseTracking.Action.IMPRESSION_ON
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.track.TrackApp
import com.tokopedia.track.interfaces.ContextAnalytics

object HomePageTrackingV2 : BaseTracking() {
    private object CustomEvent{
        const val CLICK_HOMEPAGE = "clickHomepage"
    }

    object LegoBanner{
        private const val LEGO_BANNER_4_IMAGE_NAME = "lego banner 4 image"

        fun getLegoBannerFourImageImpression(channel: DynamicHomeChannel.Channels, position: Int, isToIris: Boolean = false) = getBasicPromotionView(
                event = if(isToIris) Event.PROMO_VIEW_IRIS else Event.PROMO_VIEW,
                eventCategory = Category.HOMEPAGE,
                eventAction = Action.IMPRESSION.format(LEGO_BANNER_4_IMAGE_NAME),
                eventLabel = Label.NONE,
                promotions = channel.grids.mapIndexed { index, grid ->
                    Promotion(
                            id = "%s_%s".format(channel.id, grid.id),
                            creative = grid.attribution,
                            creativeUrl = grid.imageUrl,
                            name = Ecommerce.PROMOTION_NAME.format(position, LEGO_BANNER_4_IMAGE_NAME, channel.header.name),
                            position = (index + 1).toString()
                    )
                }
        )
        fun getLegoBannerFourImageClick(channel: DynamicHomeChannel.Channels, grid: DynamicHomeChannel.Grid, position: Int) = getBasicPromotionClick(
                event = Event.PROMO_CLICK,
                eventCategory = Category.HOMEPAGE,
                eventAction = Action.CLICK.format(LEGO_BANNER_4_IMAGE_NAME),
                eventLabel = grid.attribution,
                channelId = channel.id,
                categoryId = channel.categoryPersona,
                affinity = channel.persona,
                attribution = channel.galaxyAttribution,
                shopId = channel.brandId,
                promotions = channel.grids.map {
                    Promotion(
                            id = "%s_%s".format(channel.id, it.id),
                            creative = it.attribution,
                            creativeUrl = it.imageUrl,
                            name = Ecommerce.PROMOTION_NAME.format(position, LEGO_BANNER_4_IMAGE_NAME, channel.header.name),
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
        private const val RECOMMENDATION_LIST_CAROUSEL_PRODUCT = "dynamic channel list"
        private const val RECOMMENDATION_LIST_IMPRESSION_EVENT_ACTION = "impression on dynamic channel list"
        private const val RECOMMENDATION_LIST_CLICK_EVENT_ACTION = "click on dynamic channel list"

        fun getRecommendationListImpression(channel: DynamicHomeChannel.Channels, isToIris: Boolean = false) = getBasicProductView(
                event = if(isToIris) Event.PRODUCT_VIEW_IRIS else Event.PRODUCT_VIEW,
                eventCategory = Category.HOMEPAGE,
                eventAction = RECOMMENDATION_LIST_IMPRESSION_EVENT_ACTION,
                eventLabel = Label.NONE,
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
                            isFreeOngkir = grid.freeOngkir.isActive
                    )
                },
                list = String.format(
                        Value.LIST_WITH_HEADER, "1", RECOMMENDATION_LIST_CAROUSEL_PRODUCT, channel.header.name
                )
        )
        private fun getRecommendationListClick(channel: DynamicHomeChannel.Channels, grid: DynamicHomeChannel.Grid, position: Int) = getBasicProductChannelClick(
                event = Event.PRODUCT_CLICK,
                eventCategory = Category.HOMEPAGE,
                eventAction = RECOMMENDATION_LIST_CLICK_EVENT_ACTION,
                eventLabel = grid.attribution,
                channelId = channel.id,
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
                                isFreeOngkir = grid.freeOngkir.isActive
                        )
                ),
                list = String.format(
                        Value.LIST_WITH_HEADER, "1", RECOMMENDATION_LIST_CAROUSEL_PRODUCT, channel.header.name
                )
        )

        fun sendRecommendationListClick(channel: DynamicHomeChannel.Channels, grid: DynamicHomeChannel.Grid, position: Int) {
            getTracker().sendEnhanceEcommerceEvent(getRecommendationListClick(channel, grid, position))
        }

        private fun getRecommendationListSeeAllClick(channel: DynamicHomeChannel.Channels): HashMap<String, Any>{
            return DataLayer.mapOf(
                    Event.KEY, Event.CLICK_HOMEPAGE,
                    Category.KEY, Category.HOMEPAGE,
                    Action.KEY, "click view all on list carousel product",
                    Label.KEY, channel.header.name
            ) as HashMap<String, Any>
        }

        fun sendRecommendationListSeeAllClick(channel: DynamicHomeChannel.Channels) {
            getTracker().sendGeneralEvent(getRecommendationListSeeAllClick(channel))
        }
    }

    private fun getTracker(): ContextAnalytics {
        return TrackApp.getInstance().gtm
    }

    private fun convertRupiahToInt(rupiah: String): Int {
        var rupiah = rupiah
        rupiah = rupiah.replace("Rp", "")
        rupiah = rupiah.replace(".", "")
        rupiah = rupiah.replace(" ", "")
        return Integer.parseInt(rupiah)
    }

    object MixLeft {

        private const val LIST_MIX_LEFT = "dynamic channel left carousel"
        private const val IMPRESSION_MIX_LEFT = "impression on product dynamic channel left carousel"
        private const val CLICK_MIX_LEFT = "click on product dynamic channel left carousel"

        private const val CLICK_MIX_LEFT_LOADMORE = "click view all on dynamic channel left carousel"
        fun getMixLeftClickLoadMore(channel: DynamicHomeChannel.Channels): HashMap<String, Any> {
            return DataLayer.mapOf(
                    Event.KEY, CustomEvent.CLICK_HOMEPAGE,
                    Category.KEY, Category.HOMEPAGE,
                    Action.KEY, CLICK_MIX_LEFT_LOADMORE,
                    Label.KEY, channel.header.name,
                    Label.CHANNEL_LABEL, channel.id
            ) as HashMap<String, Any>
        }

        fun sendMixLeftClickLoadMore(channel: DynamicHomeChannel.Channels) {
            getTracker().sendGeneralEvent(getMixLeftClickLoadMore(channel))
        }

        fun getMixLeftProductView(channel: DynamicHomeChannel.Channels, isToIris: Boolean = false) = getBasicProductView(
                event = if(isToIris) Event.PRODUCT_VIEW_IRIS else Event.PRODUCT_VIEW,
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
                            isFreeOngkir = grid.freeOngkir.isActive
                    )
                },
                list = String.format(
                        Value.LIST, "1", LIST_MIX_LEFT, channel.header.name
                )
        )

        fun getMixLeftProductClick(channel: DynamicHomeChannel.Channels, grid: DynamicHomeChannel.Grid, position: Int) = getBasicProductChannelClick(
                event = Event.PRODUCT_CLICK,
                eventCategory = Category.HOMEPAGE,
                eventAction = CLICK_MIX_LEFT,
                eventLabel = channel.header.name,
                channelId = channel.id,
                products = listOf(
                        Product(
                                name = grid.name,
                                id = grid.id,
                                productPrice = grid.price,
                                brand = Value.NONE_OTHER,
                                category = Value.NONE_OTHER,
                                variant = Value.NONE_OTHER,
                                productPosition = (position + 1).toString(),
                                channelId = channel.id,
                                isFreeOngkir = grid.freeOngkir.isActive
                        )
                ),
                list = String.format(
                        Value.LIST, "1", LIST_MIX_LEFT, channel.header.name
                )
        )

        fun sendMixLeftProductClick(channel: DynamicHomeChannel.Channels, grid: DynamicHomeChannel.Grid, position: Int) {
            getTracker().sendEnhanceEcommerceEvent(getMixLeftProductClick(channel, grid, position))
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
                            creativeUrl = it.imageUrl,
                            name = Ecommerce.PROMOTION_NAME.format(position, POPULAR_KEYWORDS_NAME, keyword),
                            position = position.toString()
                    )

                })

        fun getPopularKeywordClickItem(channel: DynamicHomeChannel.Channels, position: Int, keyword: String) = getBasicPromotionClick(
                event = Event.PROMO_CLICK,
                eventCategory = Category.HOMEPAGE,
                eventAction = CLICK_POPULAR_KEYWORDS,
                eventLabel = channel.header.name,
                channelId = channel.id,
                categoryId = channel.categoryPersona,
                affinity = channel.persona,
                attribution = channel.galaxyAttribution,
                shopId = channel.brandId,
                promotions = channel.grids.map {
                    Promotion(
                            id = channel.id,
                            creative = it.attribution,
                            creativeUrl = it.imageUrl,
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
                    Label.CHANNEL_LABEL, channel.header.name
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
                            id = grid.id,
                            productPrice = convertRupiahToInt(
                                    grid.price
                            ).toString(),
                            brand = Value.NONE_OTHER,
                            category = Value.NONE_OTHER,
                            variant = Value.NONE_OTHER,
                            productPosition = (index + 1).toString(),
                            channelId = channel.id,
                            isFreeOngkir = grid.freeOngkir.isActive
                    )
                },
                list = String.format(
                        Value.LIST, "1", LIST_VALUE_SPRINT_SALE
                )
        )
        private fun getSprintSaleClick(channel: DynamicHomeChannel.Channels, grid: DynamicHomeChannel.Grid, position: Int) = getBasicProductChannelClick(
                event = Event.PRODUCT_CLICK,
                eventCategory = Category.HOMEPAGE,
                eventAction = EVENT_ACTION_SPRINT_SALE_CLICK,
                eventLabel = grid.attribution,
                channelId = channel.id,
                products = listOf(
                        Product(
                                name = grid.name,
                                id = grid.id,
                                productPrice = grid.price,
                                brand = Value.NONE_OTHER,
                                category = Value.NONE_OTHER,
                                variant = Value.NONE_OTHER,
                                productPosition = (position + 1).toString(),
                                channelId = channel.id,
                                isFreeOngkir = grid.freeOngkir.isActive
                        )
                ),
                list = String.format(
                        Value.LIST, "1", LIST_VALUE_SPRINT_SALE
                )
        )

        fun sendSprintSaleClick(channel: DynamicHomeChannel.Channels, grid: DynamicHomeChannel.Grid, position: Int) {
            getTracker().sendEnhanceEcommerceEvent(getSprintSaleClick(channel, grid, position))
        }

        private fun getSprintSaleSeeAllClick(channel: DynamicHomeChannel.Channels): HashMap<String, Any>{
            return DataLayer.mapOf(
                    Event.KEY, Event.CLICK_HOMEPAGE,
                    Category.KEY, Category.HOMEPAGE,
                    Action.KEY, EVENT_ACTION_SPRINT_SALE_CLICK_VIEW_ALL,
                    Label.KEY, Value.EMPTY,
                    ChannelId.KEY, channel.id
            ) as HashMap<String, Any>
        }

        fun sendSprintSaleSeeAllClick(channel: DynamicHomeChannel.Channels) {
            getTracker().sendGeneralEvent(getSprintSaleSeeAllClick(channel))
        }
    }
}
