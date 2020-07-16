package com.tokopedia.atc_common.domain.tracking

import android.content.Context
import com.appsflyer.AFInAppEventParameterName
import com.tokopedia.atc_common.data.model.response.DetailOccResponse
import com.tokopedia.atc_common.domain.tracking.AddToCartBaseTracking.AF_PARAM_CATEGORY
import com.tokopedia.atc_common.domain.tracking.AddToCartBaseTracking.AF_PARAM_CONTENT_ID
import com.tokopedia.atc_common.domain.tracking.AddToCartBaseTracking.AF_PARAM_CONTENT_QUANTITY
import com.tokopedia.atc_common.domain.tracking.AddToCartBaseTracking.AF_VALUE_CONTENT_TYPE
import com.tokopedia.atc_common.domain.tracking.AddToCartBaseTracking.VALUE_BEBAS_ONGKIR
import com.tokopedia.atc_common.domain.tracking.AddToCartBaseTracking.VALUE_CURRENCY
import com.tokopedia.atc_common.domain.tracking.AddToCartBaseTracking.VALUE_NONE_OTHER
import com.tokopedia.atc_common.domain.tracking.AddToCartBaseTracking.getMultiOriginAttribution
import com.tokopedia.atc_common.domain.tracking.AddToCartBaseTracking.setDefaultIfEmpty
import com.tokopedia.linker.model.LinkerData
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.user.session.UserSession
import org.json.JSONArray
import org.json.JSONObject

object AddToCartOccExternalTracking {

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
        AddToCartBaseTracking.sendEETracking(
                mutableMapOf(
                        TrackAppUtils.EVENT to EVENT_NAME_VALUE,
                        TrackAppUtils.EVENT_CATEGORY to EVENT_CATEGORY_VALUE,
                        TrackAppUtils.EVENT_ACTION to EVENT_ACTION_VALUE,
                        TrackAppUtils.EVENT_LABEL to EVENT_LABEL_VALUE,
                        PARAM_LAYOUT to "layout:${""};catName:${response.category.substringAfterLast("/")};catId:${response.categoryId.substringAfterLast("/")};",
                        PARAM_COMPONENT to "",
                        PARAM_PRODUCTID to response.productId,
                        PARAM_SHOPID to response.shopId,
                        PARAM_SHOPTYPE to setDefaultIfEmpty(response.shopType),
                        PARAM_ECOMMERCE to mutableMapOf(
                                PARAM_CURRENCYCODE to VALUE_CURRENCY,
                                PARAM_ADD to mutableMapOf(
                                        PARAM_PRODUCTS to arrayListOf(mutableMapOf(
                                                PARAM_NAME to response.productName,
                                                PARAM_ID to response.productId,
                                                PARAM_PRICE to response.price,
                                                PARAM_BRAND to setDefaultIfEmpty(response.brand),
                                                PARAM_CATEGORY to setDefaultIfEmpty(response.category),
                                                PARAM_VARIANT to setDefaultIfEmpty(response.variant),
                                                PARAM_QUANTITY to response.quantity,
                                                PARAM_DIMENSION_38 to setDefaultIfEmpty(response.trackerAttribution),
                                                PARAM_DIMENSION_40 to "",
                                                PARAM_DIMENSION_45 to response.cartId,
                                                PARAM_DIMENSION_54 to getMultiOriginAttribution(response.isMultiOrigin),
                                                PARAM_DIMENSION_79 to response.shopId,
                                                PARAM_SHOP_ID to response.shopId,
                                                PARAM_DIMENSION_80 to setDefaultIfEmpty(response.shopName),
                                                PARAM_SHOP_NAME to setDefaultIfEmpty(response.shopName),
                                                PARAM_DIMENSION_81 to response.shopType,
                                                PARAM_SHOP_TYPE to response.shopType,
                                                PARAM_PICTURE to setDefaultIfEmpty(response.picture),
                                                PARAM_URL to response.url,
                                                PARAM_CATEGORY_ID to response.categoryId,
                                                PARAM_DIMENSION_82 to response.categoryId,
                                                PARAM_DIMENSION_83 to if (response.isFreeOngkir) VALUE_BEBAS_ONGKIR else VALUE_NONE_OTHER
                                        ))
                                )
                        )
                ))
    }

    fun sendAppsFlyerTracking(response: DetailOccResponse) {
        val content = JSONArray().put(JSONObject().put(AF_PARAM_CONTENT_ID, response.productId.toString()).put(AF_PARAM_CONTENT_QUANTITY, response.quantity))
        AddToCartBaseTracking.sendAppsFlyerTracking(
                mutableMapOf(
                        AFInAppEventParameterName.CONTENT_ID to response.productId,
                        AFInAppEventParameterName.CONTENT_TYPE to AF_VALUE_CONTENT_TYPE,
                        AFInAppEventParameterName.DESCRIPTION to response.productName,
                        AFInAppEventParameterName.CURRENCY to VALUE_CURRENCY,
                        AFInAppEventParameterName.QUANTITY to response.quantity,
                        AFInAppEventParameterName.PRICE to response.price,
                        AF_PARAM_CATEGORY to response.category,
                        AFInAppEventParameterName.CONTENT to content.toString()
                )
        )
    }

    fun sendBranchIoTracking(context: Context, response: DetailOccResponse) {
        AddToCartBaseTracking.sendBranchIoTracking(createLinkerData(context, response))
    }

    private fun createLinkerData(context: Context, response: DetailOccResponse): LinkerData {
        return LinkerData().apply {
            id = response.productId.toString()
            price = response.price.toInt().toString()
            description = ""
            shopId = response.shopId.toString()
            catLvl1 = response.category
            userId = UserSession(context).userId
            currency = VALUE_CURRENCY
            quantity = response.quantity.toString()
        }
    }
}