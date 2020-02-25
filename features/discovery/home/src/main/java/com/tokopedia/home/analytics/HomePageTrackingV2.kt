package com.tokopedia.home.analytics

import com.google.android.gms.tagmanager.DataLayer
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.track.TrackApp
import com.tokopedia.track.interfaces.ContextAnalytics

object HomePageTrackingV2 {
    private object Event{
        val NONE = ""
        val KEY = "event"
        val CLICK = "click"
        val IMPRESSION = "impression"
        val CLICK_HOMEPAGE = "clickHomepage"
        val PRODUCT_VIEW = "productView"
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
        const val IMPRESSION_ON = "impression on \"%s"
        const val CLICK = "%s click"
        const val CLICK_ON = "click on \"%s"
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

    private object Value{
        const val NONE_OTHER = "none / other"
        const val LIST = "/ - p%s - %s - %s"
        fun getFreeOngkirValue(grid: DynamicHomeChannel.Grid) = if (grid.freeOngkir.isActive)"bebas ongkir" else "none / other"
    }

    private object Ecommerce {
        const val KEY = "ecommerce"
        const val PROMOTION_NAME = "/ - p%s - %s - %s"
        private const val PROMO_VIEW = "promoView"
        private const val PROMO_CLICK = "promoClick"
        private const val PRODUCT_VIEW = "productView"
        private const val PRODUCT_CLICK = "productClick"
        private const val PROMOTIONS = "promotions"
        private const val PRODUCTS = "products"

        private const val KEY_ID = "id"
        private const val KEY_NAME = "name"
        private const val KEY_CREATIVE = "creative"
        private const val KEY_CREATIVE_URL = "creative_url"
        private const val KEY_POSITION = "position"
        private const val KEY_PRICE = "price"
        private const val KEY_BRAND = "brand"
        private const val KEY_CATEGORY = "category"
        private const val KEY_VARIANT = "variant"
        private const val KEY_ATTRIBUTION = "attribution"
        private const val KEY_DIMENSION_83 = "dimension83"
        private const val KEY_DIMENSION_84 = "dimension84"

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

        fun getEcommerceProductView(products: List<Product>): Map<String, Any> {
            return DataLayer.mapOf(
                    PRODUCT_VIEW, DataLayer.listOf(
                    PRODUCTS, getProducts(products)
            )
            )
        }

        fun getEcommerceProductClick(products: List<Product>): Map<String, Any> {
            return DataLayer.mapOf(
                    PRODUCT_CLICK, DataLayer.listOf(
                    PRODUCTS, getProducts(products)
            )
            )
        }

        private fun getPromotions(promotions: List<Promotion>): List<Any>{
            val list = ArrayList<Map<String,Any>>()
            promotions.forEach { list.add(createPromotionMap(it)) }
            return DataLayer.listOf(*list.toTypedArray<Any>())
        }

        private fun getProducts(products: List<Product>): List<Any>{
            val list = ArrayList<Map<String,Any>>()
            products.forEach { list.add(createProductMap(it)) }
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

        private fun createProductMap(products: Product) : Map<String, Any>{
            val map = HashMap<String, Any>()
            map[KEY_NAME] = products.id
            map[KEY_ID] = products.name
            map[KEY_PRICE] = products.id
            map[KEY_BRAND] = products.name
            map[KEY_CATEGORY] = products.id
            map[KEY_VARIANT] = products.name
            map[KEY_POSITION] = products.id
            map[KEY_ATTRIBUTION] = products.name
            map[KEY_DIMENSION_83] = products.id
            map[KEY_DIMENSION_84] = products.name
            return map
        }
    }

    class Promotion(val id: String, val name: String, val creative: String, val creativeUrl: String, val position: String)

    class Product(val name: String, val id: String, val price: String, val brand: String, val category: String, val variant: String, val list: String, val position: String, val dimension83: String, val dimension84: String)

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

    private fun getBasicProductView(
            event: String,
            eventCategory: String,
            eventAction: String,
            eventLabel: String,
            products: List<Product>
    ): Map<String, Any>{
        return DataLayer.mapOf(
                Event.KEY, event,
                Category.KEY, eventCategory,
                Action.KEY, eventAction,
                Label.KEY, eventLabel,
                Ecommerce.KEY, Ecommerce.getEcommerceProductView(products)
        )
    }

    private fun getBasicProductClick(
            event: String,
            eventCategory: String,
            eventAction: String,
            eventLabel: String,
            channelId: String,
            products: List<Product>
    ): Map<String, Any>{
        return DataLayer.mapOf(
                Event.KEY, event,
                Category.KEY, eventCategory,
                Action.KEY, eventAction,
                Label.KEY, eventLabel,
                Label.CHANNEL_LABEL, channelId,
                Ecommerce.KEY, Ecommerce.getEcommerceProductClick(products)
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
                Event.KEY, Event.CLICK_HOMEPAGE,
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
                            price = grid.price,
                            brand = Value.NONE_OTHER,
                            category = Value.NONE_OTHER,
                            variant = Value.NONE_OTHER,
                            list = String.format(
                                    Value.LIST, "1", RECOMMENDATION_LIST_CAROUSEL_PRODUCT, channel.header.name
                            ),
                            position = (index + 1).toString(),
                            dimension83 = Value.getFreeOngkirValue(grid),
                            dimension84 = channel.id
                    )
                }
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
                                price = grid.price,
                                brand = Value.NONE_OTHER,
                                category = Value.NONE_OTHER,
                                variant = Value.NONE_OTHER,
                                list = String.format(
                                        Value.LIST, "1", RECOMMENDATION_LIST_CAROUSEL_PRODUCT, channel.header.name
                                ),
                                position = (position + 1).toString(),
                                dimension83 = Value.getFreeOngkirValue(grid),
                                dimension84 = channel.id
                        )
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
