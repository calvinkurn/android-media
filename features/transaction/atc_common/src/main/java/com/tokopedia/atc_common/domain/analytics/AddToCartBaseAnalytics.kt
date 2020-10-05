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
        val data = LinkerData.Builder.getLinkerBuilder()
                .setId(productId)
                .setProductName(productName)
                .setPrice(convertPriceToIntString(price))
                .setQuantity(quantity)
                .setCatLvl1(catLvl1)
                .setContentType(contentType)
                .setLevel1Id(level1Id)
                .setLevel1Name(level1Name)
                .setLevel2Id(level2Id)
                .setLevel2Name(level2Name)
                .setLevel3Id(level3Id)
                .setLevel3Name(level3Name)
                .setUserId(userId)
                .build()
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