package com.tokopedia.common_tradein.utils

import android.content.Context
import android.content.Intent
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalCategory
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.common_tradein.model.TradeInPDPData

object TradeInPDPHelper {
    private const val TRADE_IN_PDP_PARCEL_KEY_RESPONSE = "trade_in_pdp_page_response"
    const val TRADE_IN_PDP_CACHE_ID = "trade_in_pdp_cache_id"
    const val PARAM_DEVICE_ID = "DEVICE ID"
    const val PARAM_PHONE_TYPE = "PHONE TYPE"
    const val PARAM_PHONE_PRICE = "PHONE PRICE"

    fun pdpToTradeIn(context : Context?,
                     shopName: String,
                     shopBadge: String,
                     shopLocation: String,
                     productId: String,
                     productPrice: Int,
                     productName: String,
                     productImage: String?,
                     startActivityForResult: (Intent, Int) -> Unit) {
        context?.let {
            val cacheManager = SaveInstanceCacheManager(it, true)
            val parcelData = TradeInPDPData(
                shopName = shopName,
                shopBadge = shopBadge,
                shopLocation = shopLocation,
                productId = productId,
                productPrice = productPrice,
                productName = productName,
                productImage = productImage
            )
            cacheManager.put(TRADE_IN_PDP_PARCEL_KEY_RESPONSE, parcelData)
            val intent = RouteManager.getIntent(context, ApplinkConstInternalCategory.TRADEIN)
            intent.putExtra(TRADE_IN_PDP_CACHE_ID, cacheManager.id)
            startActivityForResult.invoke(intent, ApplinkConstInternalCategory.TRADEIN_HOME_REQUEST)
        }
    }

    fun getDataFromPDP(context: Context?, id : String): TradeInPDPData? {
        context?.let {
            val cacheManager = SaveInstanceCacheManager(it, id)
            return cacheManager.get(TRADE_IN_PDP_PARCEL_KEY_RESPONSE, TradeInPDPData::class.java)
        }
        return null
    }
}