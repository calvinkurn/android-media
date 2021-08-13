package com.tokopedia.atc_common.domain.analytics

import com.tokopedia.atc_common.data.model.response.DetailOccResponse
import com.tokopedia.atc_common.domain.analytics.AddToCartBaseAnalytics.VALUE_BEBAS_ONGKIR
import com.tokopedia.atc_common.domain.analytics.AddToCartBaseAnalytics.VALUE_BEBAS_ONGKIR_EXTRA
import com.tokopedia.atc_common.domain.analytics.AddToCartBaseAnalytics.VALUE_CURRENCY
import com.tokopedia.atc_common.domain.analytics.AddToCartBaseAnalytics.VALUE_NONE_OTHER
import com.tokopedia.track.TrackAppUtils

object AddToCartOccExternalAnalytics {

    private const val EVENT_NAME_VALUE = "addToCart"
    private const val EVENT_CATEGORY_VALUE = "product detail page"
    private const val EVENT_ACTION_VALUE = "click - Beli Langsung on pdp"
    private const val EVENT_LABEL_VALUE = "fitur : Beli Langsung occ"

    private const val PARAM_LAYOUT = "layout"
    private const val PARAM_COMPONENT = "component"
    private const val PARAM_PRODUCTID = "productId"
    private const val PARAM_SHOPID = "shopId"
    private const val PARAM_SHOPTYPE = "shopType"
    private const val PARAM_ECOMMERCE = "ecommerce"

    private const val PARAM_CURRENCYCODE = "currencyCode"
    private const val PARAM_ADD = "add"

    private const val PARAM_PRODUCTS = "products"
    private const val PARAM_NAME = "name"
    private const val PARAM_ID = "id"
    private const val PARAM_PRICE = "price"
    private const val PARAM_BRAND = "brand"
    private const val PARAM_CATEGORY = "category"
    private const val PARAM_VARIANT = "variant"
    private const val PARAM_QUANTITY = "quantity"
    private const val PARAM_DIMENSION_38 = "dimension38"
    private const val PARAM_DIMENSION_40 = "dimension40"
    private const val PARAM_DIMENSION_45 = "dimension45"
    private const val PARAM_DIMENSION_54 = "dimension54"
    private const val PARAM_DIMENSION_79 = "dimension79"
    private const val PARAM_SHOP_ID = "shop_id"
    private const val PARAM_DIMENSION_80 = "dimension80"
    private const val PARAM_SHOP_NAME = "shop_name"
    private const val PARAM_DIMENSION_81 = "dimension81"
    private const val PARAM_SHOP_TYPE = "shop_type"
    private const val PARAM_PICTURE = "picture"
    private const val PARAM_URL = "url"
    private const val PARAM_CATEGORY_ID = "category_id"
    private const val PARAM_DIMENSION_82 = "dimension82"
    private const val PARAM_DIMENSION_83 = "dimension83"

    fun sendEETracking(response: DetailOccResponse) {
        //same as atc occ in pdp
        AddToCartBaseAnalytics.sendEETracking(
                mutableMapOf(
                        TrackAppUtils.EVENT to EVENT_NAME_VALUE,
                        TrackAppUtils.EVENT_CATEGORY to EVENT_CATEGORY_VALUE,
                        TrackAppUtils.EVENT_ACTION to EVENT_ACTION_VALUE,
                        TrackAppUtils.EVENT_LABEL to EVENT_LABEL_VALUE,
                        PARAM_LAYOUT to "layout:${""};catName:${response.category.substringAfterLast("/")};catId:${response.categoryId.substringAfterLast("/")};",
                        PARAM_COMPONENT to "",
                        PARAM_PRODUCTID to response.productId,
                        PARAM_SHOPID to response.shopId,
                        PARAM_SHOPTYPE to AddToCartBaseAnalytics.setDefaultIfEmpty(response.shopType),
                        PARAM_ECOMMERCE to mutableMapOf(
                                PARAM_CURRENCYCODE to VALUE_CURRENCY,
                                PARAM_ADD to mutableMapOf(
                                        PARAM_PRODUCTS to arrayListOf(mutableMapOf(
                                                PARAM_NAME to response.productName,
                                                PARAM_ID to response.productId,
                                                PARAM_PRICE to response.price,
                                                PARAM_BRAND to AddToCartBaseAnalytics.setDefaultIfEmpty(response.brand),
                                                PARAM_CATEGORY to AddToCartBaseAnalytics.setDefaultIfEmpty(response.category),
                                                PARAM_VARIANT to AddToCartBaseAnalytics.setDefaultIfEmpty(response.variant),
                                                PARAM_QUANTITY to response.quantity,
                                                PARAM_DIMENSION_38 to AddToCartBaseAnalytics.setDefaultIfEmpty(response.trackerAttribution),
                                                PARAM_DIMENSION_40 to "",
                                                PARAM_DIMENSION_45 to response.cartId,
                                                PARAM_DIMENSION_54 to AddToCartBaseAnalytics.getMultiOriginAttribution(response.isMultiOrigin),
                                                PARAM_DIMENSION_79 to response.shopId,
                                                PARAM_SHOP_ID to response.shopId,
                                                PARAM_DIMENSION_80 to AddToCartBaseAnalytics.setDefaultIfEmpty(response.shopName),
                                                PARAM_SHOP_NAME to AddToCartBaseAnalytics.setDefaultIfEmpty(response.shopName),
                                                PARAM_DIMENSION_81 to response.shopType,
                                                PARAM_SHOP_TYPE to response.shopType,
                                                PARAM_PICTURE to AddToCartBaseAnalytics.setDefaultIfEmpty(response.picture),
                                                PARAM_URL to response.url,
                                                PARAM_CATEGORY_ID to response.categoryId,
                                                PARAM_DIMENSION_82 to response.categoryId,
                                                PARAM_DIMENSION_83 to when {
                                                    response.isFreeOngkirExtra -> VALUE_BEBAS_ONGKIR_EXTRA
                                                    response.isFreeOngkir -> VALUE_BEBAS_ONGKIR
                                                    else -> VALUE_NONE_OTHER
                                                }
                                        ))
                                )
                        )
                ))
    }
}