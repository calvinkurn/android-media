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
        val eta: Eta = Eta(),
        @SerializedName("invoice")
        @Expose
        val invoice: Invoice = Invoice(),
        @SerializedName("orderStatus")
        @Expose
        val orderStatus: OrderStatus = OrderStatus(),
    ) {
        data class Eta(
            @SerializedName("label")
            @Expose
            val label: String = "",
            @SerializedName("time")
            @Expose
            val time: String = ""
        )

        data class Invoice(
            @SerializedName("gofoodOrderNumber")
            @Expose
            val gofoodOrderNumber: String = "",
            @SerializedName("invoiceNumber")
            @Expose
            val invoiceNumber: String = ""
        )

        data class OrderStatus(
            @SerializedName("iconName")
            @Expose
            val iconName: String = "",
            @SerializedName("status")
            @Expose
            val status: String = "",
            @SerializedName("subtitle")
            @Expose
            val subtitle: String = "",
            @SerializedName("title")
            @Expose
            val title: String = ""
        )
    }
}