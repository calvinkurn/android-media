package com.tokopedia.atc_common.domain.analytics

import com.appsflyer.AFInAppEventParameterName
import com.appsflyer.AFInAppEventType
import com.tokopedia.linker.LinkerConstants
import com.tokopedia.linker.LinkerManager
import com.tokopedia.linker.LinkerUtils
import com.tokopedia.linker.model.LinkerData
import com.tokopedia.track.TrackApp
import org.json.JSONArray
import org.json.JSONObject

object AddToCartBaseAnalytics {

    private const val AF_PARAM_CATEGORY = "category"
    private const val AF_PARAM_CONTENT_ID = "id"
    private const val AF_PARAM_CONTENT_QUANTITY = "quantity"
    private const val AF_VALUE_CONTENT_TYPE = "product"

    const val VALUE_CURRENCY = "IDR"

    const val VALUE_BEBAS_ONGKIR = "bebas ongkir"
    const val VALUE_NONE_OTHER = "none / other"

    fun sendEETracking(data: MutableMap<String, Any>) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(data)
    }

    fun sendAppsFlyerTracking(productId: String, productName: String, price: String, quantity: String, category: String) {
        val content = JSONArray().put(JSONObject().put(AF_PARAM_CONTENT_ID, productId).put(AF_PARAM_CONTENT_QUANTITY, quantity))
        TrackApp.getInstance().appsFlyer.sendEvent(AFInAppEventType.ADD_TO_CART,
                mutableMapOf<String, Any>(
                        AFInAppEventParameterName.CONTENT_ID to productId,
                        AFInAppEventParameterName.CONTENT_TYPE to AF_VALUE_CONTENT_TYPE,
                        AFInAppEventParameterName.DESCRIPTION to productName,
                        AFInAppEventParameterName.CURRENCY to VALUE_CURRENCY,
                        AFInAppEventParameterName.QUANTITY to quantity,
                        AFInAppEventParameterName.PRICE to convertPriceToIntString(price),
                        AF_PARAM_CATEGORY to category,
                        AFInAppEventParameterName.CONTENT to content.toString())
        )
    }

    fun sendBranchIoTracking(productId: String, productName: String, price: String, quantity: String, catLvl1: String,
                             level1Id: String, level1Name: String, level2Id: String, level2Name: String, level3Id: String, level3Name: String,
                             contentType: String, userId: String) {
        val data = LinkerData().apply {
            this.id = productId
            this.productName = productName
            this.price = convertPriceToIntString(price)
            this.quantity = quantity
            this.catLvl1 = catLvl1
            this.contentType = contentType
            this.level1Id = level1Id
            this.level1Name = level1Name
            this.level2Id = level2Id
            this.level2Name = level2Name
            this.level3Id = level3Id
            this.level3Name = level3Name
            this.userId = userId
        }
        LinkerManager.getInstance().sendEvent(LinkerUtils.createGenericRequest(LinkerConstants.EVENT_ADD_TO_CART, data))
    }

    fun setDefaultIfEmpty(value: String?): String {
        if (value.isNullOrBlank()) {
            return VALUE_NONE_OTHER
        }
        return value
    }

    fun getMultiOriginAttribution(isMultiOrigin: Boolean): String = when (isMultiOrigin) {
        true -> "tokopedia"
        else -> "regular"
    }

    private fun convertPriceToIntString(price: String): String {
        return price.replace("[^0-9]".toRegex(), "")
    }
}