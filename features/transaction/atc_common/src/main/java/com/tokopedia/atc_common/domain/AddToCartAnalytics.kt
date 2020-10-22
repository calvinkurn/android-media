package com.tokopedia.atc_common.domain

import com.appsflyer.AFInAppEventParameterName
import com.appsflyer.AFInAppEventType
import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.track.TrackApp
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject

class AddToCartAnalytics @Inject constructor() {

    companion object {
        private const val AF_PARAM_CATEGORY = "category"
        private const val AF_PARAM_CONTENT_ID = "id"
        private const val AF_PARAM_CONTENT_QUANTITY = "quantity"
        private const val AF_VALUE_CONTENT_TYPE = "product"
        private const val AF_VALUE_CURRENCY = "IDR"
    }

    fun sendAppsFlyerTracking(result: AddToCartDataModel, addToCartRequest: AddToCartRequestParams) {
        if (!result.isDataError()) {
            val jsonArrayAfContent = JSONArray()
                    .put(JSONObject()
                            .put(AF_PARAM_CONTENT_ID, addToCartRequest.productId.toString())
                            .put(AF_PARAM_CONTENT_QUANTITY, addToCartRequest.quantity));
            TrackApp.getInstance().appsFlyer.sendEvent(AFInAppEventType.ADD_TO_CART,
                    mutableMapOf<String, Any>(
                            AFInAppEventParameterName.CONTENT_ID to addToCartRequest.productId.toString(),
                            AFInAppEventParameterName.CONTENT_TYPE to AF_VALUE_CONTENT_TYPE,
                            AFInAppEventParameterName.DESCRIPTION to addToCartRequest.productName,
                            AFInAppEventParameterName.CURRENCY to AF_VALUE_CURRENCY,
                            AFInAppEventParameterName.QUANTITY to addToCartRequest.quantity,
                            AFInAppEventParameterName.PRICE to addToCartRequest.price.replace("[^0-9]".toRegex(), ""),
                            AF_PARAM_CATEGORY to addToCartRequest.category,
                            AFInAppEventParameterName.CONTENT to jsonArrayAfContent.toString())
            )
        }
    }

}