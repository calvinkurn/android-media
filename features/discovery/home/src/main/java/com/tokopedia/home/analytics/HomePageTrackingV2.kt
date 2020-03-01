package com.tokopedia.home.analytics

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.home.analytics.v2.BaseTracking
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel

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


        fun getPopularKeywordClickReload(channel: DynamicHomeChannel.Channels): HashMap<String, Any> {
            return DataLayer.mapOf(
                    Event.KEY, CustomEvent.CLICK_HOMEPAGE,
                    Category.KEY, Category.HOMEPAGE,
                    Action.KEY, CLICK_POPULAR_KEYWORDS_RELOAD,
                    Label.KEY, channel.header.name,
                    Label.CHANNEL_LABEL, channel.header.name
            ) as HashMap<String, Any>
        }
    }
}
