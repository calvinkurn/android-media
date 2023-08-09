package com.tokopedia.promousage.data.request

import com.google.gson.annotations.SerializedName
import com.tokopedia.purchase_platform.common.feature.promo.data.request.clear.ClearPromoOrderData
import com.tokopedia.purchase_platform.common.feature.promo.data.request.clear.ClearPromoRequest

data class ClearCacheAutoApplyStackParam(
    @SerializedName("serviceID")
    val serviceId: String = "",
    @SerializedName("isOCC")
    val isOcc: Boolean = false,
    @SerializedName("orderData")
    val orderData: ClearPromoOrderData = ClearPromoOrderData()
) {
    companion object {
        fun create(
            clearPromoRequest: ClearPromoRequest
        ) : ClearCacheAutoApplyStackParam {
            return ClearCacheAutoApplyStackParam(
                clearPromoRequest.serviceId,
                clearPromoRequest.isOcc,
                clearPromoRequest.orderData
            )
        }
    }
}
