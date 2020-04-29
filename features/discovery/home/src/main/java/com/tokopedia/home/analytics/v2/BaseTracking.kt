package com.tokopedia.home.analytics.v2

import android.annotation.SuppressLint
import com.tokopedia.analyticconstant.DataLayer;
import com.tokopedia.home.analytics.v2.BaseTracking.Value.FORMAT_2_ITEMS_UNDERSCORE
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.track.TrackApp
import com.tokopedia.track.interfaces.ContextAnalytics

@SuppressLint("VisibleForTests")
abstract class BaseTracking {
    protected object Event{
        const val NONE = ""
        const val KEY = "event"
        const val CLICK = "click"
        const val IMPRESSION = "impression"
        const val PROMO_VIEW = "promoView"
        const val PRODUCT_VIEW = "productView"
        const val PRODUCT_VIEW_IRIS = "productViewIris"
        const val PROMO_CLICK = "promoClick"
        const val PRODUCT_CLICK = "productClick"
        const val PROMO_VIEW_IRIS = "promoViewIris"
        const val CLICK_HOMEPAGE = "clickHomepage"
        const val PRODUCT_ADD_TO_CART = "addToCart"
    }

    protected object Category{
        const val KEY = "eventCategory"
        const val HOMEPAGE = "homepage"
    }

    protected object Action{
        const val KEY = "eventAction"
        const val IMPRESSION = "%s impression"
        const val IMPRESSION_ON = "impression on %s"
        const val CLICK = "%s click"
        const val CLICK_ON = "click on %s"
    }

    protected object Label{
        const val KEY = "eventLabel"
        const val CHANNEL_LABEL = "channelId"
        const val AFFINITY_LABEL = "affinityLabel"
        const val ATTRIBUTION_LABEL = "attribution"
        const val CATEGORY_LABEL = "categoryId"
        const val SHOP_LABEL = "shopId"
        const val CAMPAIGN_CODE = "campaignCode"
        const val NONE = ""
        const val FORMAT_2_ITEMS = "%s - %s"
    }

    protected object Screen{
        const val KEY = "screenName"
        const val DEFAULT = "/"
    }

    protected object UserId{
        const val KEY = "userId"
        const val DEFAULT = ""
    }

    protected object CurrentSite{
        const val KEY = "currentSite"
        const val DEFAULT = "tokopediamarketplace"
    }

    protected object ChannelId{
        const val KEY = "channelId"
    }

    protected object Value{
        const val NONE_OTHER = "none / other"
        const val LIST_WITH_HEADER = "/ - p%s - %s - %s"
        const val LIST = "/ - p%s - %s"
        const val LIST_HEADER_NAME = "/ - p%s - %s - %s"
        const val EMPTY = ""
        const val FORMAT_2_ITEMS_UNDERSCORE = "%s_%s"

        fun getFreeOngkirValue(grid: DynamicHomeChannel.Grid) = if (grid.freeOngkir.isActive)"bebas ongkir" else "none / other"
    }

    object Ecommerce {
        const val KEY = "ecommerce"
        const val PROMOTION_NAME = "/ - p%s - %s - %s"
        private const val PRODUCT_VIEW = "productView"
        private const val PRODUCT_CLICK = "productClick"
        private const val CLICK = "click"
        private const val ADD = "add"
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
        private const val KEY_DIMENSION_40 = "dimension40"
        private const val KEY_DIMENSION_45 = "dimension45"
        private const val KEY_DIMENSION_83 = "dimension83"
        private const val KEY_DIMENSION_84 = "dimension84"
        private const val KEY_DIMENSION_96 = "dimension96"

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
                            LIST, list  + if(products.first().isTopAds) " - topads" else ""
                        ),
                        PRODUCTS, getProductsClick(products, list)
                    )
            )
        }
        fun getEcommerceProductAddToCart(products: List<Product>, list: String): Map<String, Any> {
            return DataLayer.mapOf(
                    CURRENCY_CODE, IDR,
                    ADD, DataLayer.mapOf(
                        ACTION_FIELD, DataLayer.mapOf(
                            LIST, list  + if(products.first().isTopAds) " - topads" else ""
                        ),
                        PRODUCTS, getProductsClick(products, list)
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

        private fun getProductsClick(products: List<Product>, listClick: String): List<Any>{
            val list = ArrayList<Map<String,Any>>()
            products.forEach { list.add(createProductMap(it, listClick)) }
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
            map[KEY_POSITION] = promotion.position
            map[KEY_PROMO_ID] = promotion.promoIds
            map[KEY_PROMO_CODE] = promotion.promoCodes
            return map
        }

        private fun createProductMap(product: Product, list: String = "") : Map<String, String>{
            val map = HashMap<String, String>()
            map[KEY_ID] = product.id
            map[KEY_NAME] = product.name
            map[KEY_BRAND] = if(product.brand.isNotBlank()) product.brand else NONE
            map[KEY_VARIANT] = if(product.variant.isNotBlank()) product.variant else NONE
            map[KEY_PRICE] = product.productPrice
            map[KEY_CATEGORY] = if(product.category.isNotBlank()) product.category else NONE
            map[KEY_POSITION] = product.productPosition
            map[KEY_DIMENSION_83] = if(product.isFreeOngkir) FREE_ONGKIR else NONE
            map[KEY_DIMENSION_40] = list + if(product.isTopAds) " - topads" else ""
            if (product.channelId.isNotEmpty()) map[KEY_DIMENSION_84] = product.channelId else NONE
            if (product.categoryId.isNotEmpty() || product.persoType.isNotEmpty()) map[KEY_DIMENSION_96] = String.format(FORMAT_2_ITEMS_UNDERSCORE, product.persoType, product.categoryId) else NONE
            if (list.isNotEmpty()) map[KEY_LIST] = list + if(product.isTopAds) " - topads" else ""
            if(product.cartId.isNotEmpty()) map[KEY_DIMENSION_45] = product.cartId
            return map
        }
    }

    class Promotion(val id: String, val name: String, val creative: String, val creativeUrl: String, val position: String, val promoIds: String = "", val promoCodes: String = "")
    open class  Product(
            val name: String,
            val id: String,
            val productPrice: String,
            val brand: String,
            val category: String,
            val variant: String,
            val productPosition: String,
            val isFreeOngkir: Boolean,
            val channelId: String = "",
            val persoType: String = "",
            val isTopAds: Boolean = false,
            val cartId: String = "",
            val categoryId: String = ""): ImpressHolder()

    open fun getBasicPromotionView(
        event: String,
        eventCategory: String,
        eventAction: String,
        eventLabel: String,
        promotions: List<Promotion>,
        userId: String = ""
    ): Map<String, Any>{
        return DataLayer.mapOf(
                Event.KEY, event,
                Category.KEY, eventCategory,
                Action.KEY, eventAction,
                Label.KEY, eventLabel,
                UserId.KEY, userId,
                Ecommerce.KEY, Ecommerce.getEcommercePromoView(promotions)
        )
    }

    open fun getBasicPromotionChannelView(
            event: String,
            eventCategory: String,
            eventAction: String,
            eventLabel: String,
            promotions: List<Promotion>,
            channelId: String,
            userId: String = ""
    ): Map<String, Any>{
        return DataLayer.mapOf(
                Event.KEY, event,
                Category.KEY, eventCategory,
                Action.KEY, eventAction,
                Label.KEY, eventLabel,
                UserId.KEY, userId,
                Ecommerce.KEY, Ecommerce.getEcommercePromoView(promotions),
                ChannelId.KEY, channelId
        )
    }

    open fun getBasicPromotionChannelClick(
            event: String,
            eventCategory: String,
            eventAction: String,
            eventLabel: String,
            channelId: String,
            affinity: String,
            attribution: String,
            categoryId: String,
            shopId: String,
            campaignCode: String,
            promotions: List<Promotion>,
            userId: String = ""
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
                Label.CAMPAIGN_CODE, campaignCode,
                UserId.KEY, userId,
                Ecommerce.KEY, Ecommerce.getEcommercePromoClick(promotions),
                ChannelId.KEY, channelId
        )
    }

    open fun getBasicProductClick(
            event: String,
            eventCategory: String,
            eventAction: String,
            eventLabel: String,
            list: String,
            products: List<Product>,
            userId: String = ""
    ): Map<String, Any>{
        return DataLayer.mapOf(
                Event.KEY, event,
                Category.KEY, eventCategory,
                Action.KEY, eventAction,
                Label.KEY, eventLabel,
                UserId.KEY, userId,
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
            campaignCode: String,
            products: List<Product>,
            userId: String = ""
    ): Map<String, Any>{
        return DataLayer.mapOf(
                Event.KEY, event,
                Category.KEY, eventCategory,
                Action.KEY, eventAction,
                Label.KEY, eventLabel,
                Label.CHANNEL_LABEL, channelId,
                Label.CAMPAIGN_CODE, campaignCode,
                UserId.KEY, userId,
                Ecommerce.KEY, Ecommerce.getEcommerceProductClick(products, list)
        )
    }

    open fun getBasicProductView(
            event: String,
            eventCategory: String,
            eventAction: String,
            eventLabel: String,
            list: String,
            products: List<Product>,
            userId: String = ""
    ): Map<String, Any>{
        return DataLayer.mapOf(
                Event.KEY, event,
                Category.KEY, eventCategory,
                Action.KEY, eventAction,
                Label.KEY, eventLabel,
                UserId.KEY, userId,
                Ecommerce.KEY, Ecommerce.getEcommerceProductView(products, list)
        )
    }

    open fun getBasicProductChannelView(
            event: String,
            eventCategory: String,
            eventAction: String,
            eventLabel: String,
            list: String,
            products: List<Product>,
            channelId: String,
            userId: String = ""
    ): Map<String, Any>{
        return DataLayer.mapOf(
                Event.KEY, event,
                Category.KEY, eventCategory,
                Action.KEY, eventAction,
                Label.KEY, eventLabel,
                UserId.KEY, userId,
                Ecommerce.KEY, Ecommerce.getEcommerceProductView(products, list),
                ChannelId.KEY, channelId
        )
    }

    protected fun convertRupiahToInt(rupiah: String): Int {
        try {
            var rupiah = rupiah
            rupiah = rupiah.replace("Rp", "")
            rupiah = rupiah.replace(".", "")
            rupiah = rupiah.replace(" ", "")
            return Integer.parseInt(rupiah)
        } catch (e: Exception) {
            return 0
        }
    }

    protected fun getTracker(): ContextAnalytics {
        return TrackApp.getInstance().gtm
    }
}
