package com.tokopedia.purchase_platform.common.feature.promo.data.request.clear

import com.google.gson.annotations.SerializedName

data class ClearPromoRequest(
        @SerializedName("serviceID")
        val serviceId: String = "",
        @SerializedName("isOCC")
        val isOcc: Boolean = false,
        @SerializedName("orderData")
        val orderData: ClearPromoOrderData = ClearPromoOrderData()
)

data class ClearPromoOrderData(
        @SerializedName("codes")
        val codes: List<String> = emptyList(),
        @SerializedName("orders")
        val orders: List<ClearPromoOrder> = emptyList()
)

data class ClearPromoOrder(
        @SerializedName("uniqueID")
        val uniqueId: String = "",
        @SerializedName("boType")
        val boType: Int = 0,
        @SerializedName("codes")
        val codes: MutableList<String> = ArrayList()
)