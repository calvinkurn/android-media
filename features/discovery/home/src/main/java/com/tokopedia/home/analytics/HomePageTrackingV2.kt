package com.tokopedia.home.analytics

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.home.analytics.v2.BaseTracking
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
                promotions = listOf(
                        Promotion(
                                id = "%s_%s".format(channel.id, grid.id),
                                creative = grid.attribution,
                                creativeUrl = grid.imageUrl,
                                name = Ecommerce.PROMOTION_NAME.format(position, LEGO_BANNER_4_IMAGE_NAME, channel.header.name),
                                position = position.toString()
                        )
                )
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
        private const val RECOMMENDATION_LIST_CAROUSEL_PRODUCT = "carousel product"

        fun getRecommendationListImpression(channel: DynamicHomeChannel.Channels, isToIris: Boolean = false) = getBasicProductView(
                event = if(isToIris) Event.PROMO_VIEW_IRIS else Event.PRODUCT_VIEW,
                eventCategory = Category.HOMEPAGE,
                eventAction = Action.IMPRESSION_ON.format(RECOMMENDATION_LIST_CAROUSEL_PRODUCT),
                eventLabel = Label.NONE,
                products = channel.grids.mapIndexed { index, grid ->
                    Product(
                            name = grid.name,
                            id = grid.id,
                            productPrice = grid.price,
                            brand = Value.NONE_OTHER,
                            category = Value.NONE_OTHER,
                            variant = Value.NONE_OTHER,
                            productPosition = (index + 1).toString(),
                            dimension83 = "",
                            dimension84 = channel.id,
                            list = "",
                            isFreeOngkir = grid.freeOngkir.isActive
                    )
                },
                list = String.format(
                        Value.LIST, "1", RECOMMENDATION_LIST_CAROUSEL_PRODUCT, channel.header.name
                )
        )
        private fun getRecommendationListClick(channel: DynamicHomeChannel.Channels, grid: DynamicHomeChannel.Grid, position: Int) = getBasicProductClick(
                event = Event.PROMO_CLICK,
                eventCategory = Category.HOMEPAGE,
                eventAction = Action.CLICK_ON.format(RECOMMENDATION_LIST_CAROUSEL_PRODUCT),
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
                                list = "",
                                productPosition = (position + 1).toString(),
                                dimension84 = channel.id,
                                isFreeOngkir = grid.freeOngkir.isActive,
                                dimension83 = ""
                        )
                ),
                list = String.format(
                        Value.LIST, "1", RECOMMENDATION_LIST_CAROUSEL_PRODUCT, channel.header.name
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
}
