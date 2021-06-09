package com.tokopedia.track.builder.util

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.track.TrackApp
import com.tokopedia.track.builder.util.BaseTrackerConst.Value.FORMAT_2_ITEMS_UNDERSCORE
import com.tokopedia.track.interfaces.ContextAnalytics

/**
 * @author by yoasfs on 01/09/20
 */
abstract class BaseTrackerConst {
    protected object Event{
        const val NONE = ""
        const val KEY = "event"
        const val CLICK = "click"
        const val IMPRESSION = "impression"
        const val PROMO_VIEW = "promoView"
        const val OPEN_SCREEN = "openScreen"
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
        const val HOMEPAGE_TOPADS = "homepage-topads"
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

    protected object BusinessUnit{
        const val KEY = "businessUnit"
        const val DEFAULT = "home & browse"
    }

    protected object ChannelId{
        const val KEY = "channelId"
    }

    protected object CampaignCode{
        const val KEY = "campaignCode"
    }

    protected object Value {
        const val NONE_OTHER = "none / other"
        const val LIST_WITH_HEADER = "/ - p%s - %s - %s"
        const val LIST = "/ - p%s - %s"
        const val LIST_HEADER_NAME = "/ - p%s - %s - %s"
        const val EMPTY = ""
        const val FORMAT_2_ITEMS_UNDERSCORE = "%s_%s"
        const val FORMAT_2_ITEMS_DASH = "%s - %s"
    }

    class Promotion(
            val id: String,
            val name: String,
            val creative: String,
            val position: String,
            val promoIds: String = "",
            val promoCodes: String = "",
            val creativeUrl: String = "")

    open class  Product(
            val name: String,
            val id: String,
            val productPrice: String,
            val brand: String,
            val category: String,
            val variant: String,
            val productPosition: String,
            val isFreeOngkir: Boolean,
            val isFreeOngkirExtra: Boolean = false,
            val channelId: String = "",
            val persoType: String = "",
            val isTopAds: Boolean? = false,
            val cartId: String = "",
            val categoryId: String = "",
            val clusterId: Int = -1,
            val quantity: String = "",
            val headerName: String? = null,
            val isCarousel: Boolean? = null,
            val recommendationType: String = "",
            val shopId:String = "",
            val pageName: String = "")

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
        private const val FREE_ONGKIR_EXTRA = "bebas ongkir extra"
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
        private const val KEY_QUANTITY = "quantity"
        private const val KEY_DIMENSION_11 = "dimension11"
        private const val KEY_DIMENSION_40 = "dimension40"
        private const val KEY_DIMENSION_45 = "dimension45"
        private const val KEY_DIMENSION_79 = "dimension79"
        private const val KEY_DIMENSION_80 = "dimension80"
        private const val KEY_DIMENSION_81 = "dimension81"
        private const val KEY_DIMENSION_82 = "dimension82"
        private const val KEY_DIMENSION_83 = "dimension83"
        private const val KEY_DIMENSION_84 = "dimension84"
        private const val KEY_DIMENSION_96 = "dimension96"

        fun getEcommercePromoView(promotions: List<Promotion>): Map<String, Any> {
            return DataLayer.mapOf(
                    PROMO_VIEW, getPromotionsMap(promotions))
        }

        fun getEcommerceObjectPromoView(promotions: List<Any>?): Map<String, Any> {
            return DataLayer.mapOf(
                    PROMO_VIEW,
                    DataLayer.mapOf(PROMOTIONS, DataLayer.listOf(
                            promotions
                    ))
            )
        }

        fun getEcommercePromoClick(promotions: List<Promotion>): Map<String, Any> {
            return DataLayer.mapOf(
                    PROMO_CLICK, getPromotionsMap(promotions))
        }

        private fun getPromotionsMap(promotions: List<Promotion>): Map<String, Any> {
            return DataLayer.mapOf(PROMOTIONS, getPromotions(promotions))
        }

        fun getEcommerceProductClick(products: List<Product>, list: String, buildCustomList: ((Product) -> String)?): Map<String, Any> {
            return DataLayer.mapOf(
                    CURRENCY_CODE, IDR,
                    CLICK, DataLayer.mapOf(
                    ACTION_FIELD, DataLayer.mapOf(
                    LIST, if (list.isEmpty()) setNewList(products.firstOrNull(), list) else list
            ),
                    PRODUCTS, getProductsClick(products, if (list.isEmpty()) setNewList(products.firstOrNull(), list) else list, buildCustomList)
            )
            )
        }
        fun getEcommerceProductAddToCart(products: List<Product>, list: String): Map<String, Any> {
            return DataLayer.mapOf(
                    CURRENCY_CODE, IDR,
                    ADD, DataLayer.mapOf(
                    ACTION_FIELD, DataLayer.mapOf(
                    LIST, setNewList(products.firstOrNull(), list)
            ),
                    PRODUCTS, getProductsClick(products, list)
            )
            )
        }

        fun getEcommerceProductView(products: List<Product>, list: String, buildCustomList: ((Product) -> String)?): Map<String, Any> {
            return DataLayer.mapOf(
                    CURRENCY_CODE, IDR,
                    IMPRESSIONS, getProductsImpression(products, list, buildCustomList)
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

        private fun getProductsClick(products: List<Product>, listClick: String, buildCustomList: ((Product) -> String)? = null): List<Any>{
            val list = ArrayList<Map<String,Any>>()
            products.forEach { list.add(createProductMap(it, listClick, buildCustomList)) }
            return DataLayer.listOf(*list.toTypedArray<Any>())
        }

        private fun getProductsImpression(products: List<Product>, listImpression: String, buildCustomList: ((Product) -> String)? = null): List<Any>{
            val list = ArrayList<Map<String,Any>>()
            products.forEach { list.add(createProductMap(it, listImpression, buildCustomList)) }
            return DataLayer.listOf(*list.toTypedArray<Any>())
        }

        private fun createPromotionMap(promotion: Promotion) : Map<String, String>{
            val map = HashMap<String, String>()
            map[KEY_ID] = promotion.id
            map[KEY_NAME] = promotion.name
            map[KEY_CREATIVE] = promotion.creative
            map[KEY_CREATIVE_URL] = promotion.creativeUrl
            map[KEY_POSITION] = promotion.position
            if(promotion.promoIds.isNotBlank()) map[KEY_PROMO_ID] = promotion.promoIds
            if(promotion.promoCodes.isNotBlank()) map[KEY_PROMO_CODE] = promotion.promoCodes
            return map
        }

        private fun createProductMap(product: Product, list: String = "", buildCustomList: ((Product) -> String)? = null) : Map<String, String>{
            val map = HashMap<String, String>()
            map[KEY_ID] = product.id
            map[KEY_NAME] = product.name
            map[KEY_BRAND] = if(product.brand.isNotBlank()) product.brand else NONE
            map[KEY_VARIANT] = if(product.variant.isNotBlank()) product.variant else NONE
            map[KEY_PRICE] = product.productPrice
            map[KEY_CATEGORY] = if(product.category.isNotBlank()) product.category else NONE
            map[KEY_POSITION] = product.productPosition
            map[KEY_DIMENSION_83] = checkBebasOngkir(product)
            map[KEY_DIMENSION_40] = buildCustomList?.invoke(product) ?: if(list.isEmpty()) setNewList(product, list) else list
            if(product.clusterId != -1) map[KEY_DIMENSION_11] = product.clusterId.toString()
            if (product.channelId.isNotEmpty()) map[KEY_DIMENSION_84] = product.channelId else NONE
            if (product.categoryId.isNotEmpty() || product.persoType.isNotEmpty()) map[KEY_DIMENSION_96] = String.format(FORMAT_2_ITEMS_UNDERSCORE, product.persoType, product.categoryId) else NONE
            if (list.isNotEmpty()) map[KEY_LIST] = if (list.isEmpty()) setNewList(product, list) else list
            if(product.cartId.isNotEmpty()) map[KEY_DIMENSION_45] = product.cartId
            if(product.cartId.isNotEmpty()) map[KEY_DIMENSION_79] = product.shopId
            if(product.cartId.isNotEmpty()) map[KEY_DIMENSION_80] = NONE
            if(product.cartId.isNotEmpty()) map[KEY_DIMENSION_81] = ""
            if(product.cartId.isNotEmpty()) map[KEY_DIMENSION_82] = NONE
            if(product.quantity.isNotEmpty()) map[KEY_QUANTITY] = product.quantity
            return map
        }

        private fun checkBebasOngkir(product: Product): String{
            return when {
                product.isFreeOngkirExtra -> FREE_ONGKIR_EXTRA
                product.isFreeOngkir -> FREE_ONGKIR
                else -> NONE
            }
        }

        private fun setNewList(product: Product?, list: String): String{
            if(product == null) return list
            var newList = list + if(product.isTopAds == true) " - topads" else if(product.isTopAds == false) " - non topads" else ""
            if(product.isCarousel != null) newList += if (product.isCarousel == true) " - carousel" else "- non carousel"
            if(product.recommendationType.isNotEmpty()) newList += " - ${product.recommendationType}"
            if(product.pageName.isNotEmpty()) newList += " - ${product.pageName}"
            if(product.headerName != null) newList += " - ${product.headerName}"
            return newList
        }
    }

    protected fun convertRupiahToInt(rupiah: String): Int {
        return try {
            var newRupiah = rupiah
            newRupiah = newRupiah.replace("Rp", "")
            newRupiah = newRupiah.replace(".", "")
            newRupiah = newRupiah.replace(" ", "")
            Integer.parseInt(newRupiah)
        } catch (e: Exception) {
            0
        }
    }

    protected fun getTracker(): ContextAnalytics {
        return TrackApp.getInstance().gtm
    }
}