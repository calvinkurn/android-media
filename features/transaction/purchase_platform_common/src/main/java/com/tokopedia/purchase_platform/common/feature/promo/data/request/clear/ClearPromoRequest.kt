package com.tokopedia.purchase_platform.common.feature.promo.data.request.clear

import com.google.gson.annotations.SerializedName

data class ClearPromoRequest(
        @SerializedName("serviceID")
        var serviceId: String = "",
        @SerializedName("isOCC")
        var isOcc: Boolean = false,
        @SerializedName("orderData")
        var orderData: ClearPromoOrderData = ClearPromoOrderData()
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
        val codes: MutableList<String> = ArrayList(),
        @SerializedName("shopID")
        val shopId: Long = 0,
        @SerializedName("isPO")
        val isPo: Boolean = false,
        @SerializedName("duration")
        val poDuration: String = "0",
        @SerializedName("warehouseID")
        val warehouseId: Long = 0,
)