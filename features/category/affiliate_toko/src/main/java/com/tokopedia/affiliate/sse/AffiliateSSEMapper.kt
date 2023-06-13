package com.tokopedia.affiliate.sse

import com.google.gson.Gson
import com.tokopedia.affiliate.sse.model.AffiliateSSEAdpTotalClick
import com.tokopedia.affiliate.sse.model.AffiliateSSEAdpTotalClickItem
import com.tokopedia.affiliate.sse.model.AffiliateSSEResponse
import com.tokopedia.config.GlobalConfig
import timber.log.Timber

class AffiliateSSEMapper(private val response: AffiliateSSEResponse) {

    private val gson = Gson()

    fun mapping(): Any? {
        return when (response.event) {
            AffiliateSSEType.AffiliateSSE.type -> mapAffiliateSSE()
            else -> null
        }
    }

    private fun mapAffiliateSSE(): Any? {
        return when {
            response.message.contains("TRAFFIC_CLICK_ADP_ITEM") -> convertToModel(
                response.message,
                AffiliateSSEAdpTotalClickItem::class.java
            )
            response.message.contains("TRAFFIC_CLICK_ADP") -> convertToModel(
                response.message,
                AffiliateSSEAdpTotalClick::class.java
            )
            else -> null
        }
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
