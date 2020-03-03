package com.tokopedia.home.analytics.v2

import com.tokopedia.analyticconstant.DataLayer;
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.kotlin.model.ImpressHolder

abstract class BaseTracking {
    protected object Event{
        const val NONE = ""
        const val KEY = "event"
        const val CLICK = "click"
        const val IMPRESSION = "impression"
        const val PROMO_VIEW = "promoView"
        const val PRODUCT_VIEW = "productView"
        const val PROMO_CLICK = "promoClick"
        const val PRODUCT_CLICK = "productClick"
        const val PROMO_VIEW_IRIS = "promoViewIris"
        const val CLICK_HOMEPAGE = "clickHomepage"
    }

    protected object Category{
        const val KEY = "eventCategory"
        const val HOMEPAGE = "homepage"
    }

    protected object Action{
        const val KEY = "eventAction"
        const val IMPRESSION = "%s impression"
        const val IMPRESSION_ON = "impression on \"%s"
        const val CLICK = "%s click"
        const val CLICK_ON = "click on \"%s"
    }

    protected object Label{
        const val KEY = "eventLabel"
        const val CHANNEL_LABEL = "channelId"
        const val AFFINITY_LABEL = "affinityLabel"
        const val ATTRIBUTION_LABEL = "attribution"
        const val CATEGORY_LABEL = "categoryId"
        const val SHOP_LABEL = "shopId"
        const val NONE = ""
        const val FORMAT_2_ITEMS = "%s - %s"
    }

    protected object Value{
        const val NONE_OTHER = "none / other"
        const val LIST = "/ - p%s - %s - %s"
        fun getFreeOngkirValue(grid: DynamicHomeChannel.Grid) = if (grid.freeOngkir.isActive)"bebas ongkir" else "none / other"
    }

    protected object Ecommerce {
        const val KEY = "ecommerce"
        const val PROMOTION_NAME = "/ - p%s - %s - %s"
        private const val PRODUCT_VIEW = "productView"
        private const val PRODUCT_CLICK = "productClick"
        private const val CLICK = "click"
        private const val IMPRESSIONS = "impressions"
        private const val PROMO_VIEW = "promoView"
        private const val PROMO_CLICK = "promoClick"
        private const val PROMOTIONS = "promotions"
        private const val PRODUCTS = "products"
        private const val ACTION_FIELD = "actionField"
        private const val CURRENCY_CODE = "currencyCode"
        private const val IDR = "IDR"
        private const val LIST = "list"
        private const val FREE_ONGKIR = "bebas ongkir"
        private const val NONE = "none / other"


        private const val KEY_ID = "id"
        private const val KEY_NAME = "name"
        private const val KEY_CREATIVE = "creative"
        private const val KEY_CREATIVE_URL = "creative_url"
        private const val KEY_PROMO_ID = "promo_id"
        private const val KEY_PROMO_CODE = "promo_code"
        private const val KEY_PRICE = "price"
        private const val KEY_BRAND = "brand"
        private const val KEY_VARIANT = "variant"
        private const val KEY_CATEGORY = "category"
        private const val KEY_POSITION = "position"
        private const val KEY_LIST = "list"
        private const val KEY_ATTRIBUTION = "attribution"
        private const val KEY_DIMENSION_83 = "dimension83"
        private const val KEY_DIMENSION_84 = "dimension84"

        fun getEcommercePromoView(promotions: List<Promotion>): Map<String, Any> {
            return DataLayer.mapOf(
                    PROMO_VIEW, getPromotionsMap(promotions))
        }

        fun getEcommercePromoClick(promotions: List<Promotion>): Map<String, Any> {
            return DataLayer.mapOf(
                    PROMO_CLICK, getPromotionsMap(promotions))
        }

        private fun getPromotionsMap(promotions: List<Promotion>): Map<String, Any> {
            return DataLayer.mapOf(PROMOTIONS, getPromotions(promotions))
        }

        fun getEcommerceProductClick(products: List<Product>, list: String): Map<String, Any> {
            return DataLayer.mapOf(
                    CURRENCY_CODE, IDR,
                    CLICK, DataLayer.mapOf(
                        ACTION_FIELD, DataLayer.mapOf(
                            LIST, list
                        ),
                        PRODUCTS, getProducts(products)
                    )
            )
        }

        fun getEcommerceProductView(products: List<Product>, list: String): Map<String, Any> {
            return DataLayer.mapOf(
                    CURRENCY_CODE, IDR,
                    IMPRESSIONS, getProductsImpression(products, list)
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

        private fun getProductsImpression(products: List<Product>, listImpression: String): List<Any>{
            val list = ArrayList<Map<String,Any>>()
            products.forEach { list.add(createProductMap(it, listImpression)) }
            return DataLayer.listOf(*list.toTypedArray<Any>())
        }

        private fun createPromotionMap(promotion: Promotion) : Map<String, String>{
            val map = HashMap<String, String>()
            map[KEY_ID] = promotion.id
            map[KEY_NAME] = promotion.name
            map[KEY_CREATIVE] = promotion.creative
            map[KEY_CREATIVE_URL] = promotion.creativeUrl
            map[KEY_POSITION] = promotion.position.toString()
            map[KEY_PROMO_ID] = promotion.promoIds
            map[KEY_PROMO_CODE] = promotion.promoCodes
            return map
        }

        private fun createProductMap(product: Product, list: String = "") : Map<String, String>{
            val map = HashMap<String, String>()
            map[KEY_ID] = product.id
            map[KEY_NAME] = product.name
            map[KEY_BRAND] = product.brand
            map[KEY_VARIANT] = product.variant
            map[KEY_PRICE] = product.productPrice.toString()
            map[KEY_CATEGORY] = product.category
            map[KEY_POSITION] = product.productPosition.toString()
            map[KEY_DIMENSION_83] = if(product.isFreeOngkir) FREE_ONGKIR else NONE
            if (product.channelId.isNotEmpty()) map[KEY_DIMENSION_84] = product.channelId else NONE
            if (list.isNotEmpty()) map[KEY_LIST] = list
            return map
        }
    }

    class Promotion(val id: String, val name: String, val creative: String, val creativeUrl: String, val position: String, val promoIds: String = "", val promoCodes: String = "")
    open class Product(
            val name: String,
            val id: String,
            val productPrice: String,
            val brand: String,
            val category: String,
            val variant: String,
            val productPosition: String,
            val isFreeOngkir: Boolean,
            val channelId: String = ""): ImpressHolder()

    open fun getBasicPromotionView(
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

    open fun getBasicPromotionClick(
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

    open fun getBasicProductClick(
            event: String,
            eventCategory: String,
            eventAction: String,
            eventLabel: String,
            list: String,
            products: List<Product>
    ): Map<String, Any>{
        return DataLayer.mapOf(
                Event.KEY, event,
                Category.KEY, eventCategory,
                Action.KEY, eventAction,
                Label.KEY, eventLabel,
                Ecommerce.KEY, Ecommerce.getEcommerceProductClick(products, list)
        )
    }

    open fun getBasicProductChannelClick(
            event: String,
            eventCategory: String,
            eventAction: String,
            eventLabel: String,
            list: String,
            channelId: String,
            products: List<Product>
    ): Map<String, Any>{
        return DataLayer.mapOf(
                Event.KEY, event,
                Category.KEY, eventCategory,
                Action.KEY, eventAction,
                Label.KEY, eventLabel,
                Label.CHANNEL_LABEL, channelId,
                Ecommerce.KEY, Ecommerce.getEcommerceProductClick(products, list)
        )
    }

    open fun getBasicProductView(
            event: String,
            eventCategory: String,
            eventAction: String,
            eventLabel: String,
            list: String,
            products: List<Product>
    ): Map<String, Any>{
        return DataLayer.mapOf(
                Event.KEY, event,
                Category.KEY, eventCategory,
                Action.KEY, eventAction,
                Label.KEY, eventLabel,
                Ecommerce.KEY, Ecommerce.getEcommerceProductView(products, list)
        )
    }
}
