package com.tokopedia.atc_common.domain

import android.os.Bundle
import com.appsflyer.AFInAppEventParameterName
import com.appsflyer.AFInAppEventType
import com.tokopedia.atc_common.domain.model.response.atcexternal.AddToCartExternalDataModel
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject

class AddToCartExternalAnalytics @Inject constructor() {

    companion object {
        // Apsflyer constants
        private const val AF_PARAM_CATEGORY = "category"
        private const val AF_PARAM_CONTENT_ID = "id"
        private const val AF_PARAM_CONTENT_QUANTITY = "quantity"
        private const val AF_VALUE_CONTENT_TYPE = "product"
        private const val AF_VALUE_CURRENCY = "IDR"

        // Enhanced Ecommerce constants

    }

    fun sendEnhancedEcommerceTracking(data: AddToCartExternalDataModel) {
        val itemBundle = Bundle()
        itemBundle.putString("item_id", setValueOrDefault(data.productId.toString()))
        itemBundle.putString("item_name", setValueOrDefault(data.productName))
        itemBundle.putString("item_brand", setValueOrDefault(data.brand))
        itemBundle.putString("item_category", setValueOrDefault(data.category))
        itemBundle.putString("item_variant", setValueOrDefault(data.variant))
        itemBundle.putString("shop_id", setValueOrDefault(data.shopId.toString()))
        itemBundle.putString("shop_name", setValueOrDefault(data.shopName))
        itemBundle.putString("shop_type", setValueOrDefault(data.shopType))
        itemBundle.putString("category_id", setValueOrDefault(data.categoryId))
        itemBundle.putInt("quantity", data.quantity.coerceAtLeast(1))
        itemBundle.putInt("price", data.price)
        itemBundle.putString("picture", data.picture)
        itemBundle.putString("url", data.url)
        itemBundle.putString("dimension38", setValueOrDefault(data.trackerAttribution))
        itemBundle.putString("dimension45", setValueOrDefault(data.cartId.toString()))
        itemBundle.putString("dimension54", if(data.isMultiOrigin) "tokopedia" else "regular")
        itemBundle.putString("dimension83", if (data.isFreeOngkir) "bebas ongkir" else "none/other")

        val eventDataLayer = Bundle()
        eventDataLayer.putString(TrackAppUtils.EVENT, "add_to_cart")
        eventDataLayer.putString(TrackAppUtils.EVENT_CATEGORY, "cart")
        eventDataLayer.putString(TrackAppUtils.EVENT_ACTION, "click add to cart")
        eventDataLayer.putString(TrackAppUtils.EVENT_LABEL, "")
        eventDataLayer.putParcelableArrayList("items", arrayListOf(itemBundle))

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent("view_item", eventDataLayer)
    }

    private fun setValueOrDefault(value: String): String {
        if (value.isBlank()) {
            return "none/other"
        }
        return value
    }

    fun sendAppsFlyerTracking(data: AddToCartExternalDataModel) {
        TrackApp.getInstance().appsFlyer.sendEvent(AFInAppEventType.ADD_TO_CART,
                mutableMapOf(
                        AFInAppEventParameterName.CONTENT_ID to data.productId.toString(),
                        AFInAppEventParameterName.CONTENT_TYPE to AF_VALUE_CONTENT_TYPE,
                        AFInAppEventParameterName.DESCRIPTION to data.productName,
                        AFInAppEventParameterName.CURRENCY to AF_VALUE_CURRENCY,
                        AFInAppEventParameterName.QUANTITY to data.quantity,
                        AFInAppEventParameterName.PRICE to data.price,
                        AF_PARAM_CATEGORY to data.category,
                        AFInAppEventParameterName.CONTENT to JSONArray().put(JSONObject()
                                .put(AF_PARAM_CONTENT_ID, data.productId.toString())
                                .put(AF_PARAM_CONTENT_QUANTITY, data.quantity))
                )
        )
    }

}