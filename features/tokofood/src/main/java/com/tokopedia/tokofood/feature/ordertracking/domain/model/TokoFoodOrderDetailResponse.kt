package com.tokopedia.tokofood.feature.ordertracking.domain.model


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class TokoFoodOrderDetailResponse(
    @SerializedName("tokofoodOrderDetail")
    @Expose
    val tokofoodOrderDetail: TokofoodOrderDetail = TokofoodOrderDetail()
) {
    data class TokofoodOrderDetail(
        @SerializedName("actionButtons")
        @Expose
        val actionButtons: List<ActionButton> = listOf(),
        @SerializedName("additionalTickerInfo")
        @Expose
        val additionalTickerInfo: List<AdditionalTickerInfo> = listOf(),
        @SerializedName("destination")
        @Expose
        val destination: Destination = Destination(),
        @SerializedName("dotMenus")
        @Expose
        val dotMenus: List<DotMenu> = listOf(),
        @SerializedName("driverDetails")
        @Expose
        val driverDetails: DriverDetails = DriverDetails(),
        @SerializedName("eta")
        @Expose
        val eta: Eta = Eta(),
        @SerializedName("invoice")
        @Expose
        val invoice: Invoice = Invoice(),
        @SerializedName("items")
        @Expose
        val items: List<Item> = listOf(),
        @SerializedName("merchant")
        @Expose
        val merchant: Merchant = Merchant(),
        @SerializedName("orderStatus")
        @Expose
        val orderStatus: OrderStatus = OrderStatus(),
        @SerializedName("payment")
        @Expose
        val payment: Payment = Payment()
    ) {
        data class ActionButton(
            @SerializedName("actionType")
            @Expose
            val actionType: String = "",
            @SerializedName("appUrl")
            @Expose
            val appUrl: String = "",
            @SerializedName("label")
            @Expose
            val label: String = "",
            @SerializedName("webUrl")
            @Expose
            val webUrl: String = ""
        )

        data class AdditionalTickerInfo(
            @SerializedName("appUrl")
            @Expose
            val appUrl: String = "",
            @SerializedName("level")
            @Expose
            val level: String = "",
            @SerializedName("notes")
            @Expose
            val notes: String = "",
            @SerializedName("urlText")
            @Expose
            val urlText: String = "",
            @SerializedName("webUrl")
            @Expose
            val webUrl: String = ""
        )

        data class Destination(
            @SerializedName("info")
            @Expose
            val info: String = "",
            @SerializedName("label")
            @Expose
            val label: String = "",
            @SerializedName("phone")
            @Expose
            val phone: String = ""
        )

        data class DotMenu(
            @SerializedName("actionType")
            @Expose
            val actionType: String = "",
            @SerializedName("appUrl")
            @Expose
            val appUrl: String = "",
            @SerializedName("label")
            @Expose
            val label: String = "",
            @SerializedName("webUrl")
            @Expose
            val webUrl: String = ""
        )

        data class DriverDetails(
            @SerializedName("karma")
            @Expose
            val karma: List<Karma> = listOf(),
            @SerializedName("licensePlateNumber")
            @Expose
            val licensePlateNumber: String = "",
            @SerializedName("name")
            @Expose
            val name: String = "",
            @SerializedName("photoUrl")
            @Expose
            val photoUrl: String = ""
        ) {
            data class Karma(
                @SerializedName("icon")
                @Expose
                val icon: String = "",
                @SerializedName("message")
                @Expose
                val message: String = ""
            )
        }

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

        data class Item(
            @SerializedName("displayName")
            @Expose
            val displayName: String = "",
            @SerializedName("notes")
            @Expose
            val notes: String = "",
            @SerializedName("price")
            @Expose
            val price: Double = 0.0,
            @SerializedName("quantity")
            @Expose
            val quantity: Int = 0,
            @SerializedName("variants")
            @Expose
            val variants: List<Variant> = listOf()
        ) {
            data class Variant(
                @SerializedName("displayName")
                @Expose
                val displayName: String = "",
                @SerializedName("optionName")
                @Expose
                val optionName: String = ""
            )
        }

        data class Merchant(
            @SerializedName("displayName")
            @Expose
            val displayName: String = "",
            @SerializedName("distanceInKm")
            @Expose
            val distanceInKm: String = ""
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

        data class Payment(
            @SerializedName("paymentAmount")
            @Expose
            val paymentAmount: PaymentAmount = PaymentAmount(),
            @SerializedName("paymentDate")
            @Expose
            val paymentDate: String = "",
            @SerializedName("paymentDetails")
            @Expose
            val paymentDetails: List<PaymentDetail> = listOf(),
            @SerializedName("paymentMethod")
            @Expose
            val paymentMethod: PaymentMethod = PaymentMethod()
        ) {
            data class PaymentAmount(
                @SerializedName("label")
                @Expose
                val label: String = "",
                @SerializedName("value")
                @Expose
                val value: String = ""
            )

            data class PaymentDetail(
                @SerializedName("label")
                @Expose
                val label: String = "",
                @SerializedName("value")
                @Expose
                val value: String = ""
            )

            data class PaymentMethod(
                @SerializedName("label")
                @Expose
                val label: String = "",
                @SerializedName("value")
                @Expose
                val value: String = ""
            )
        }
    }
}