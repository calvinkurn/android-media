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

    fun sendEETracking(response: DetailOccResponse) {
        //same as atc occ in pdp
        AddToCartBaseTracking.sendEETracking(
                mutableMapOf(
                        TrackAppUtils.EVENT to "addToCart",
                        TrackAppUtils.EVENT_CATEGORY to "product detail page",
                        TrackAppUtils.EVENT_ACTION to "click - Beli Langsung on pdp",
                        TrackAppUtils.EVENT_LABEL to "fitur : Beli Langsung occ",
                        "layout" to "layout:${""};catName:${response.category.substringAfterLast("/")};catId:${response.categoryId.substringAfterLast("/")};",
                        "component" to "",
                        "productId" to response.productId,
                        "shopId" to response.shopId,
                        "shopType" to setDefaultIfEmpty(response.shopType),
                        "ecommerce" to mutableMapOf(
                                "currencyCode" to VALUE_CURRENCY,
                                "add" to mutableMapOf(
                                        "products" to arrayListOf(mutableMapOf(
                                                "name" to response.productName,
                                                "id" to response.productId,
                                                "price" to response.price,
                                                "brand" to setDefaultIfEmpty(response.brand),
                                                "category" to setDefaultIfEmpty(response.category),
                                                "variant" to setDefaultIfEmpty(response.variant),
                                                "quantity" to response.quantity,
                                                "dimension38" to setDefaultIfEmpty(response.trackerAttribution),
                                                "dimension40" to "",
                                                "dimension45" to response.cartId,
                                                "dimension54" to getMultiOriginAttribution(response.isMultiOrigin),
                                                "dimension79" to response.shopId,
                                                "shop_id" to response.shopId,
                                                "dimension80" to setDefaultIfEmpty(response.shopName),
                                                "shop_name" to setDefaultIfEmpty(response.shopName),
                                                "dimension81" to response.shopType,
                                                "shop_type" to response.shopType,
                                                "picture" to setDefaultIfEmpty(response.picture),
                                                "url" to response.url,
                                                "category_id" to response.categoryId,
                                                "dimension82" to response.categoryId,
                                                "dimension83" to if (response.isFreeOngkir) VALUE_BEBAS_ONGKIR else VALUE_NONE_OTHER
                                        ))
                                )
                        )
                ))
    }

    fun sendAppsFlyerTracking(response: DetailOccResponse) {
        AddToCartBaseTracking.sendAppsFlyerTracking(
                mutableMapOf(
                        AFInAppEventParameterName.CONTENT_ID to response.productId,
                        AFInAppEventParameterName.CONTENT_TYPE to AF_VALUE_CONTENT_TYPE,
                        AFInAppEventParameterName.DESCRIPTION to response.productName,
                        AFInAppEventParameterName.CURRENCY to VALUE_CURRENCY,
                        AFInAppEventParameterName.QUANTITY to response.quantity,
                        AFInAppEventParameterName.PRICE to response.price,
                        AF_PARAM_CATEGORY to response.category,
                        AFInAppEventParameterName.CONTENT to JSONArray().put(JSONObject().put(AF_PARAM_CONTENT_ID, response.productId.toString()).put(AF_PARAM_CONTENT_QUANTITY, response.quantity))
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