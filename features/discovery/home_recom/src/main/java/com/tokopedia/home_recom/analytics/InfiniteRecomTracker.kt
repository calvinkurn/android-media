package com.tokopedia.home_recom.analytics

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.track.builder.BaseTrackerBuilder
import com.tokopedia.track.builder.util.BaseTrackerConst

/**
 * Created by yfsx on 22/09/21.
 */
object InfiniteRecomTracker : BaseTrackerConst() {

    private const val IMPRESSION_ACTION_INFINITE_RECOM_ITEM = "impression - tokonow product recommendation pdp"
    private const val IMPRESSION_CATEGORY_INFINITE_RECOM_ITEM = "tokonow product detail page"
    private const val CLICK_ACTION_INFINITE_RECOM_ITEM = "click - tokonow product recommendation pdp"
    private const val CLICK_CATEGORY_INFINITE_RECOM_ITEM = "tokonow product detail page"
    private const val BU_RECOM = "tokopediamarketplace"
    private const val CURRENTSITE_RECOM = "tokopediamarketplace"
    private const val KEY_PRODUCT_ID = "productId"
    private const val KEY_PAGE_SOURCE = "pageSource"
    private const val VALUE_EVENT_LABEL = "%s,%s"
    private const val VALUE_PAGE_SOURCE = "%s.%s" //first value is page source PDP, recommendationType || second value is recomItem.recommendationType
    private const val CONST_LIST = "/tokonow - recomproduct - %s - rekomendasi untuk anda - %s" //first value page name, second one recommendation type

    private const val KEY_SHOP_ID_SELLER = "shop_id"
    private const val KEY_SHOP_TYPE = "shop_type"
    private const val KEY_SHOP_NAME = "shop_name"
    private const val KEY_ECOMMERCE = "ecommerce"
    private const val KEY_CATEGORY_ID = "category_id"
    private const val KEY_USER_ID_VARIANT = "userId"

    private const val ID = "id"
    private const val NAME = "name"

    private const val PRODUCTS = "products"
    private const val PRICE = "price"
    private const val BRAND = "brand"
    private const val VARIANT = "variant"
    private const val QUANTITY = "quantity"
    private const val CATEGORY = "category"
    private const val CURRENCY_CODE = "currencyCode"
    private const val CURRENCY_DEFAULT_VALUE = "IDR"
    private const val VALUE_NONE_OTHER = "none / other"
    private const val KEY_ADD = "add"
    private const val KEY_REMOVE = "remove"
    private const val KEY_DIMENSION_40 = "dimension40"
    private const val KEY_DIMENSION_45 = "dimension45"
    private const val VALUE_INFINITE_RECOM = "infinite recom"


    fun eventRecomItemImpression(recomWidget: RecommendationWidget?, recomItem: RecommendationItem, userId: String, parentProductid: String): Map<String, Any> {
        val trackerBuilder = BaseTrackerBuilder().constructBasicProductView(
                event = Event.PRODUCT_VIEW,
                eventAction = IMPRESSION_ACTION_INFINITE_RECOM_ITEM,
                eventCategory = IMPRESSION_CATEGORY_INFINITE_RECOM_ITEM,
                eventLabel = String.format(VALUE_EVENT_LABEL, recomWidget?.title, ""),
                list = String.format(CONST_LIST, recomItem.pageName, recomItem.recommendationType),
                products = listOf(BaseTrackerConst.Product(
                        productPosition = recomItem.position.toString(),
                        brand = "",
                        category = "",
                        id = recomItem.productId.toString(),
                        name = recomItem.name,
                        variant = "",
                        productPrice = CURRENCY_DEFAULT_VALUE,
                        isFreeOngkir = recomItem.isFreeOngkirActive
                ))
        )
        trackerBuilder.appendUserId(userId)
        trackerBuilder.appendBusinessUnit(BU_RECOM)
        trackerBuilder.appendCurrentSite(CURRENTSITE_RECOM)
        trackerBuilder.appendCustomKeyValue(KEY_PRODUCT_ID, parentProductid)
        trackerBuilder.appendCustomKeyValue(KEY_PAGE_SOURCE, String.format(VALUE_PAGE_SOURCE, VALUE_INFINITE_RECOM, recomItem.recommendationType))
        return trackerBuilder.build()
    }

    fun eventRecomItemClick(recomWidget: RecommendationWidget?, recomItem: RecommendationItem, userId: String, parentProductid: String): Map<String, Any> {
        val trackerBuilder = BaseTrackerBuilder().constructBasicProductClick(
                event = Event.PRODUCT_CLICK,
                eventAction = CLICK_ACTION_INFINITE_RECOM_ITEM,
                eventCategory = CLICK_CATEGORY_INFINITE_RECOM_ITEM,
                eventLabel = String.format(VALUE_EVENT_LABEL, recomWidget?.title, ""),
                list = String.format(CONST_LIST, recomItem.pageName, recomItem.recommendationType),
                products = listOf(BaseTrackerConst.Product(
                        productPosition = recomItem.position.toString(),
                        brand = "",
                        category = "",
                        id = recomItem.productId.toString(),
                        name = recomItem.name,
                        variant = "",
                        productPrice = CURRENCY_DEFAULT_VALUE,
                        isFreeOngkir = recomItem.isFreeOngkirActive
                ))
        )
        trackerBuilder.appendUserId(userId)
        trackerBuilder.appendBusinessUnit(BU_RECOM)
        trackerBuilder.appendCurrentSite(CURRENTSITE_RECOM)
        trackerBuilder.appendCustomKeyValue(KEY_PRODUCT_ID, parentProductid)
        trackerBuilder.appendCustomKeyValue(KEY_PAGE_SOURCE, String.format(VALUE_PAGE_SOURCE, VALUE_INFINITE_RECOM, recomItem.recommendationType))
        return trackerBuilder.build()
    }

    fun eventClickRecomAddToCart(recomWidget: RecommendationWidget?, recomItem: RecommendationItem, userId: String, quantity: Int, parentProductid: String): Map<String, Any> {
        val trackerBuilder = BaseTrackerBuilder()
        trackerBuilder.appendEvent(RecomTokonow.KEY_EVENT_ATC)
        trackerBuilder.appendEventCategory(RecomTokonow.KEY_EVENT_CATEGORY_ATC)
        trackerBuilder.appendEventAction(RecomTokonow.KEY_EVENT_ACTION_ATC)
        trackerBuilder.appendEventLabel(String.format(RecomTokonow.KEY_EVENT_LABEL_ATC, recomWidget?.title, ""))
        trackerBuilder.appendBusinessUnit(BU_RECOM)
        trackerBuilder.appendCurrentSite(CURRENTSITE_RECOM)
        trackerBuilder.appendCustomKeyValue(KEY_PRODUCT_ID, parentProductid)
        trackerBuilder.appendCustomKeyValue(KEY_USER_ID_VARIANT, userId)
        trackerBuilder.appendCustomKeyValue(RecomTokonow.KEY_EVENT_PAGE_SOURCE, String.format(VALUE_PAGE_SOURCE, VALUE_INFINITE_RECOM, recomItem.recommendationType))
        trackerBuilder.appendCustomKeyValue(KEY_ECOMMERCE, DataLayer.mapOf(
                CURRENCY_CODE, CURRENCY_DEFAULT_VALUE,
                KEY_ADD, DataLayer.mapOf(
                PRODUCTS, DataLayer.listOf(
                DataLayer.mapOf(
                        BRAND, VALUE_NONE_OTHER,
                        CATEGORY, "",
                        KEY_CATEGORY_ID, "",
                        KEY_DIMENSION_40, String.format(RecomTokonow.PARAM_ATC_DIMENS_40, recomItem.pageName, recomItem.recommendationType),
                        KEY_DIMENSION_45, recomItem.cartId,
                        ID, recomItem.productId,
                        NAME, recomItem.name,
                        PRICE, recomItem.priceInt,
                        QUANTITY, quantity,
                        KEY_SHOP_ID_SELLER, recomItem.shopId,
                        KEY_SHOP_NAME, recomItem.shopName,
                        KEY_SHOP_TYPE, recomItem.shopType,
                        VARIANT, ""
                )))))
        return trackerBuilder.build()
    }

    fun eventClickRecomRemoveFromCart(recomWidget: RecommendationWidget?, recomItem: RecommendationItem, userId: String, quantity: Int, parentProductid: String): Map<String, Any> {
        val trackerBuilder = BaseTrackerBuilder()
        trackerBuilder.appendEvent(RecomTokonow.KEY_EVENT_REMOVE_FROM_CART)
        trackerBuilder.appendEventCategory(RecomTokonow.KEY_EVENT_CATEGORY_ATC)
        trackerBuilder.appendEventAction(RecomTokonow.KEY_EVENT_ACTION_REMOVE_FROM_CART)
        trackerBuilder.appendEventLabel("")
        trackerBuilder.appendBusinessUnit(BU_RECOM)
        trackerBuilder.appendCurrentSite(CURRENTSITE_RECOM)
        trackerBuilder.appendCustomKeyValue(KEY_USER_ID_VARIANT, userId)
        trackerBuilder.appendCustomKeyValue(KEY_PRODUCT_ID, parentProductid)
        trackerBuilder.appendCustomKeyValue(RecomTokonow.KEY_EVENT_PAGE_SOURCE, String.format(VALUE_PAGE_SOURCE, VALUE_INFINITE_RECOM, recomItem.recommendationType))
        trackerBuilder.appendCustomKeyValue(KEY_ECOMMERCE, DataLayer.mapOf(
                CURRENCY_CODE, CURRENCY_DEFAULT_VALUE,
                KEY_REMOVE, DataLayer.mapOf(
                PRODUCTS, DataLayer.listOf(
                DataLayer.mapOf(
                        BRAND, VALUE_NONE_OTHER,
                        CATEGORY, "",
                        KEY_CATEGORY_ID, "",
                        KEY_DIMENSION_40, String.format(RecomTokonow.PARAM_ATC_DIMENS_40, recomItem.pageName, recomItem.recommendationType),
                        KEY_DIMENSION_45, recomItem.cartId,
                        ID, recomItem.productId,
                        NAME, recomItem.name,
                        PRICE, recomItem.priceInt,
                        QUANTITY, quantity,
                        KEY_SHOP_ID_SELLER, recomItem.shopId,
                        KEY_SHOP_NAME, recomItem.shopName,
                        KEY_SHOP_TYPE, recomItem.shopType,
                        VARIANT, ""
                )))))
        return trackerBuilder.build()
    }

    object RecomTokonow {
        //recomatc
        const val KEY_EVENT_ATC = "addToCart"
        const val KEY_EVENT_REMOVE_FROM_CART = "removeFromCart"
        const val KEY_EVENT_PAGE_SOURCE = "pageSource"
        const val KEY_EVENT_CATEGORY_ATC = "tokonow product detail page"
        const val KEY_EVENT_ACTION_ATC = "click add to cart on tokonow product recommendation pdp"
        const val KEY_EVENT_ACTION_REMOVE_FROM_CART = "click delete on tokonow pdp product recommendation"
        const val KEY_EVENT_LABEL_ATC = "%s, %s" //recom title, chips value

        // example /tokonow - recomproduct - pdp_1 - rekomendasi untuk anda - {recommendation type}
        const val PARAM_ATC_DIMENS_40 = "/tokonow - recomproduct - %s - rekomendasi untuk anda - %s"
    }
}