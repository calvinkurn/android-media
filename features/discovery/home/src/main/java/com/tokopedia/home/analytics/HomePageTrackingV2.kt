package com.tokopedia.home.analytics

import com.google.android.gms.tagmanager.DataLayer
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel

object HomePageTrackingV2 {
    private object Event{
        val NONE = ""
        val KEY = "event"
        val CLICK = "click"
        val IMPRESSION = "impression"
        val CLICK_HOMEPAGE = "clickHomepage"
        val PROMO_VIEW = "promoView"
        val PROMO_CLICK = "promoClick"
        val PROMO_VIEW_IRIS = "promoViewIris"
    }

    private object Category{
        val KEY = "eventCategory"
        val HOMEPAGE = "homepage"
    }

    private object Action{
        const val KEY = "eventAction"
        const val IMPRESSION = "%s impression"
        const val CLICK = "%s click"
    }

    private object Label{
        const val KEY = "eventLabel"
        const val CHANNEL_LABEL = "channelId"
        const val AFFINITY_LABEL = "affinityLabel"
        const val ATTRIBUTION_LABEL = "attribution"
        const val CATEGORY_LABEL = "categoryId"
        const val SHOP_LABEL = "shopId"
        const val NONE = ""
    }

    private object Ecommerce {
        const val KEY = "ecommerce"
        const val PROMOTION_NAME = "/ - p%s - %s - %s"
        private const val PROMO_VIEW = "promoView"
        private const val PROMO_CLICK = "promoClick"
        private const val PROMOTIONS = "promotions"
        private const val KEY_ID = "id"
        private const val KEY_NAME = "name"
        private const val KEY_CREATIVE = "creative"
        private const val KEY_CREATIVE_URL = "creative_url"
        private const val KEY_POSITION = "position"


        fun getEcommercePromoView(promotions: List<Promotion>): Map<String, Any> {
            return DataLayer.mapOf(
                PROMO_VIEW, DataLayer.listOf(
                    PROMOTIONS, getPromotions(promotions)
                )
            )
        }

        fun getEcommercePromoClick(promotions: List<Promotion>): Map<String, Any> {
            return DataLayer.mapOf(
                PROMO_CLICK, DataLayer.listOf(
                    PROMOTIONS, getPromotions(promotions)
                )
            )
        }

        private fun getPromotions(promotions: List<Promotion>): List<Any>{
            val list = ArrayList<Map<String,Any>>()
            promotions.forEach { list.add(createPromotionMap(it)) }
            return DataLayer.listOf(*list.toTypedArray<Any>())
        }

        private fun createPromotionMap(promotion: Promotion) : Map<String, Any>{
            val map = HashMap<String, Any>()
            map[KEY_ID] = promotion.id
            map[KEY_NAME] = promotion.name
            map[KEY_CREATIVE] = promotion.creative
            map[KEY_CREATIVE_URL] = promotion.creativeUrl
            map[KEY_POSITION] = promotion.position
            return map
        }
    }

    class Promotion(val id: String, val name: String, val creative: String, val creativeUrl: String, val position: String)

    private fun getBasicPromotionView(
        event: String,
        eventCategory: String,
        eventAction: String,
        eventLabel: String,
        promotions: List<Promotion>
    ): Map<String, Any>{
        return DataLayer.mapOf(
                Event.KEY, event,
                Category.KEY, eventCategory,
                Action.KEY, eventAction,
                Label.KEY, eventLabel,
                Ecommerce.KEY, Ecommerce.getEcommercePromoView(promotions)
        )
    }

    private fun getBasicPromotionClick(
        event: String,
        eventCategory: String,
        eventAction: String,
        eventLabel: String,
        channelId: String,
        affinity: String,
        attribution: String,
        categoryId: String,
        shopId: String,
        promotions: List<Promotion>
    ): Map<String, Any>{
        return DataLayer.mapOf(
                Event.KEY, event,
                Category.KEY, eventCategory,
                Action.KEY, eventAction,
                Label.KEY, eventLabel,
                Label.CHANNEL_LABEL, channelId,
                Label.AFFINITY_LABEL, affinity,
                Label.ATTRIBUTION_LABEL, attribution,
                Label.CATEGORY_LABEL, categoryId,
                Label.SHOP_LABEL, shopId,
                Ecommerce.KEY, Ecommerce.getEcommercePromoClick(promotions)
        )
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
                Event.KEY, Event.CLICK_HOMEPAGE,
                Category.KEY, Category.HOMEPAGE,
                Action.KEY, Action.CLICK.format(LEGO_BANNER_4_IMAGE_NAME) + " view all",
                Label.KEY, channel.header.name,
                Label.CHANNEL_LABEL, channel.id
            ) as HashMap<String, Any>
        }
    }
}
