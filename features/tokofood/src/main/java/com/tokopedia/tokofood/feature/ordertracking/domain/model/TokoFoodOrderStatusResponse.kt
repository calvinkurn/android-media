package com.tokopedia.tokofood.feature.ordertracking.domain.model

import com.google.gson.annotations.SerializedName

data class TokoFoodOrderStatusResponse(
    @SerializedName("tokofoodOrderDetail")
    val tokofoodOrderDetail: TokofoodOrderDetail = TokofoodOrderDetail()
) {
    data class TokofoodOrderDetail(
        @SerializedName("additionalTickerInfo")
        val additionalTickerInfo: List<TokoFoodOrderDetailResponse.TokofoodOrderDetail.AdditionalTickerInfo>? = listOf(),
        @SerializedName("eta")
        val eta: TokoFoodOrderDetailResponse.TokofoodOrderDetail.Eta? = TokoFoodOrderDetailResponse.TokofoodOrderDetail.Eta(),
        @SerializedName("invoice")
        val invoice: TokoFoodOrderDetailResponse.TokofoodOrderDetail.Invoice = TokoFoodOrderDetailResponse.TokofoodOrderDetail.Invoice(),
        @SerializedName("orderStatus")
        val orderStatus: TokoFoodOrderDetailResponse.TokofoodOrderDetail.OrderStatus = TokoFoodOrderDetailResponse.TokofoodOrderDetail.OrderStatus(),
        @SerializedName("payment")
        val payment: TokoFoodOrderDetailResponse.TokofoodOrderDetail.Payment = TokoFoodOrderDetailResponse.TokofoodOrderDetail.Payment(),
        @SerializedName("driverDetails")
        val driverDetails: TokoFoodOrderDetailResponse.TokofoodOrderDetail.DriverDetails? = TokoFoodOrderDetailResponse.TokofoodOrderDetail.DriverDetails(),
    )
}
