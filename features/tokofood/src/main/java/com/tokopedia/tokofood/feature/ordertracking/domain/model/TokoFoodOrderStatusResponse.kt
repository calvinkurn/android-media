package com.tokopedia.tokofood.feature.ordertracking.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TokoFoodOrderStatusResponse(
    @SerializedName("tokofoodOrderDetail")
    @Expose
    val tokofoodOrderDetail: TokofoodOrderDetail = TokofoodOrderDetail()
) {
    data class TokofoodOrderDetail(
        @SerializedName("eta")
        @Expose
        val eta: TokoFoodOrderDetailResponse.TokofoodOrderDetail.Eta = TokoFoodOrderDetailResponse.TokofoodOrderDetail.Eta(),
        @SerializedName("invoice")
        @Expose
        val invoice: TokoFoodOrderDetailResponse.TokofoodOrderDetail.Invoice = TokoFoodOrderDetailResponse.TokofoodOrderDetail.Invoice(),
        @SerializedName("orderStatus")
        @Expose
        val orderStatus: TokoFoodOrderDetailResponse.TokofoodOrderDetail.OrderStatus = TokoFoodOrderDetailResponse.TokofoodOrderDetail.OrderStatus(),
    )
}