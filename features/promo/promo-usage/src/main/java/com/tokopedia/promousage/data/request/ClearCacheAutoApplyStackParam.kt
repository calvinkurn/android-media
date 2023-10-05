package com.tokopedia.promousage.data.request

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam
import com.tokopedia.purchase_platform.common.feature.promo.data.request.clear.ClearPromoOrderData
import com.tokopedia.purchase_platform.common.feature.promo.data.request.clear.ClearPromoRequest

data class ClearCacheAutoApplyStackParam(
    @SerializedName("serviceID")
    var serviceId: String = "",
    @SerializedName("isOCC")
    var isOcc: Boolean = false,
    @SerializedName("orderData")
    var orderData: ClearPromoOrderData = ClearPromoOrderData()
) : GqlParam {

    companion object {

        fun create(clearPromoRequest: ClearPromoRequest): ClearCacheAutoApplyStackParam {
            return ClearCacheAutoApplyStackParam(
                serviceId = clearPromoRequest.serviceId,
                isOcc = clearPromoRequest.isOcc,
                orderData = clearPromoRequest.orderData
            )
        }
    }
}
