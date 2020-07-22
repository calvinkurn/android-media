package com.tokopedia.atc_common.domain.tracking

import com.appsflyer.AFInAppEventType
import com.tokopedia.linker.LinkerConstants
import com.tokopedia.linker.LinkerManager
import com.tokopedia.linker.LinkerUtils
import com.tokopedia.linker.model.LinkerData
import com.tokopedia.track.TrackApp

object AddToCartBaseTracking {

    const val AF_PARAM_CATEGORY = "category"
    const val AF_PARAM_CONTENT_ID = "id"
    const val AF_PARAM_CONTENT_QUANTITY = "quantity"
    const val AF_VALUE_CONTENT_TYPE = "product"

    const val VALUE_CURRENCY = "IDR"

    const val VALUE_BEBAS_ONGKIR = "bebas ongkir"
    const val VALUE_NONE_OTHER = "none / other"

    fun sendEETracking(data: MutableMap<String, Any>) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(data)
    }

    fun sendAppsFlyerTracking(data: MutableMap<String, Any>) {
        TrackApp.getInstance().appsFlyer.sendEvent(AFInAppEventType.ADD_TO_CART, data)
    }

    fun sendBranchIoTracking(data: LinkerData) {
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
}