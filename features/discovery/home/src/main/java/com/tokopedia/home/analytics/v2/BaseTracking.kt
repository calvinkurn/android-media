package com.tokopedia.home.analytics.v2

import com.google.android.gms.tagmanager.DataLayer
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
    }

    protected object Category{
        const val KEY = "eventCategory"
        const val HOMEPAGE = "homepage"
    }

    protected object Action{
        const val KEY = "eventAction"
        const val IMPRESSION = "%s impression"
        const val CLICK = "%s click"
    }

    protected object Label{
        const val KEY = "eventLabel"
        const val CHANNEL_LABEL = "channelId"
        const val AFFINITY_LABEL = "affinityLabel"
        const val ATTRIBUTION_LABEL = "attribution"
        const val CATEGORY_LABEL = "categoryId"
        const val SHOP_LABEL = "shopId"
        const val NONE = ""
    }

    protected object Ecommerce {
        const val KEY = "ecommerce"
        const val PROMOTION_NAME = "/ - p%s - %s - %s"
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
        private const val KEY_PRICE = "price"
        private const val KEY_BRAND = "brand"
        private const val KEY_VARIANT = "variant"
        private const val KEY_CATEGORY = "category"
        private const val KEY_POSITION = "position"
        private const val KEY_LIST = "list"
        private const val KEY_DIMENSION_83 = "dimension83"


        fun getEcommercePromoView(promotions: List<Promotion>): Map<String, Any> {
            return DataLayer.mapOf(
                    PROMO_VIEW, DataLayer.listOf(
                    PROMOTIONS, getPromotions(promotions)
            )
            )
        }

        fun getEcommercePromoClick(promotions: List<Promotion>): Map<String, Any> {
            return DataLayer.mapOf(
                    CLICK, DataLayer.listOf(
                    PROMOTIONS, getPromotions(promotions)
            )
            )
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

        private fun createPromotionMap(promotion: Promotion) : Map<String, Any>{
            val map = HashMap<String, Any>()
            map[KEY_ID] = promotion.id
            map[KEY_NAME] = promotion.name
            map[KEY_CREATIVE] = promotion.creative
            map[KEY_CREATIVE_URL] = promotion.creativeUrl
            map[KEY_POSITION] = promotion.position
            return map
        }

        private fun createProductMap(product: Product) : Map<String, Any>{
            val map = HashMap<String, Any>()
            map[KEY_ID] = product.id
            map[KEY_NAME] = product.name
            map[KEY_BRAND] = product.brand
            map[KEY_VARIANT] = product.variant
            map[KEY_PRICE] = product.productPrice
            map[KEY_CATEGORY] = product.category
            map[KEY_POSITION] = product.productPosition.toString()
            map[KEY_DIMENSION_83] = if(product.freeOngkir) FREE_ONGKIR else NONE
            return map
        }
        private fun createProductMap(product: Product, list: String) : Map<String, Any>{
            val map = HashMap<String, Any>()
            map[KEY_ID] = product.id
            map[KEY_NAME] = product.name
            map[KEY_BRAND] = product.brand
            map[KEY_VARIANT] = product.variant
            map[KEY_PRICE] = product.productPrice
            map[KEY_CATEGORY] = product.category
            map[KEY_POSITION] = product.productPosition.toString()
            map[KEY_LIST] = list
            map[KEY_DIMENSION_83] = if(product.freeOngkir) FREE_ONGKIR else NONE
            return map
        }
    }

    class Promotion(val id: String, val name: String, val creative: String, val creativeUrl: String, val position: String)
    open class Product(val id: String, val name: String, val productPrice: Int, val brand: String, val variant: String, val category: String, val productPosition: Int, val freeOngkir: Boolean): ImpressHolder()

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