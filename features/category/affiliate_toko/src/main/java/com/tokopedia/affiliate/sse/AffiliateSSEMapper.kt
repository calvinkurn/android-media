package com.tokopedia.affiliate.sse

import com.google.gson.Gson
import com.tokopedia.affiliate.sse.model.AffiliateSSEAdpTotalClickItem
import com.tokopedia.affiliate.sse.model.AffiliateSSEResponse
import com.tokopedia.config.GlobalConfig
import timber.log.Timber

class AffiliateSSEMapper(private val response: AffiliateSSEResponse) {

    private val gson = Gson()

    fun mapping(): Any? {
        return when (response.event) {
            AffiliateSSEType.TrafficClickAdp.type -> mapTrafficClick()
            AffiliateSSEType.TrafficClickAdpItem.type -> mapTrafficClick()
            else -> null
        }
    }

    private fun mapTrafficClick(): AffiliateSSEAdpTotalClickItem? {
        return convertToModel(response.message, AffiliateSSEAdpTotalClickItem::class.java)
    }

    private fun <T> convertToModel(message: String, classOfT: Class<T>): T? {
        try {
            return gson.fromJson(message, classOfT)
        } catch (e: Exception) {
            if (!GlobalConfig.DEBUG) {
                Timber.d(e.localizedMessage)
            }
        }
        return null
    }
}
