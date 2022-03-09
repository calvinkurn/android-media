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
                     shopID: String,
                     shopName: String,
                     shopBadge: String,
                     shopLocation: String,
                     productId: String,
                     productPrice: Int,
                     productName: String,
                     productImage: String?,
                     minOrder: Int,
                     selectedWarehouseId: Int,
                     trackerAttributionPdp: String,
                     trackerListNamePdp: String,
                     shippingMinimumPrice: Double,
                     getProductName: String,
                     categoryName: String) {
        context?.let {
            val cacheManager = SaveInstanceCacheManager(it, true)
            val parcelData = TradeInPDPData(
                shopID = shopID,
                shopName = shopName,
                shopBadge = shopBadge,
                shopLocation = shopLocation,
                productId = productId,
                productPrice = productPrice,
                productName = productName,
                productImage = productImage,
                minOrder = minOrder,
                selectedWarehouseId = selectedWarehouseId,
                trackerAttributionPdp =trackerAttributionPdp,
                trackerListNamePdp = trackerListNamePdp,
                shippingMinimumPrice = shippingMinimumPrice,
                getProductName = getProductName,
                categoryName = categoryName
            )
            cacheManager.put(TRADE_IN_PDP_PARCEL_KEY_RESPONSE, parcelData)
            val intent = RouteManager.getIntent(context, ApplinkConstInternalCategory.TRADEIN)
            intent.putExtra(TRADE_IN_PDP_CACHE_ID, cacheManager.id)
            context.startActivity(intent)
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