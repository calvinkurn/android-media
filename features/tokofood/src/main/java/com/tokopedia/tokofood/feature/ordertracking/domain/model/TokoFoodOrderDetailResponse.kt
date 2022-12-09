package com.tokopedia.tokofood.feature.ordertracking.domain.model


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class TokoFoodOrderDetailResponse(
    @SerializedName("tokofoodOrderDetail")
    val tokofoodOrderDetail: TokofoodOrderDetail = TokofoodOrderDetail()
) {
    data class TokofoodOrderDetail(
        @SerializedName("actionButtons")
        val actionButtons: List<ActionButton> = listOf(),
        @SerializedName("additionalTickerInfo")
        val additionalTickerInfo: List<AdditionalTickerInfo>? = listOf(),
        @SerializedName("destination")
        val destination: Destination = Destination(),
        @SerializedName("dotMenus")
        val dotMenus: List<DotMenu>? = listOf(),
        @SerializedName("driverDetails")
        val driverDetails: DriverDetails? = DriverDetails(),
        @SerializedName("eta")
        val eta: Eta? = Eta(),
        @SerializedName("invoice")
        val invoice: Invoice = Invoice(),
        @SerializedName("items")
        val items: List<Item> = listOf(),
        @SerializedName("merchant")
        val merchant: Merchant = Merchant(),
        @SerializedName("orderStatus")
        val orderStatus: OrderStatus = OrderStatus(),
        @SerializedName("payment")
        val payment: Payment = Payment()
    ) {
        data class ActionButton(
            @SerializedName("actionType")
            val actionType: String = "",
            @SerializedName("appUrl")
            val appUrl: String = "",
            @SerializedName("label")
            val label: String = ""
        )

        data class DotMenu(
            @SerializedName("label")
            val label: String = "",
            @SerializedName("actionType")
            val actionType: String = "",
            @SerializedName("appUrl")
            val appUrl: String = "",
        )

        data class AdditionalTickerInfo(
            @SerializedName("appText")
            val appText: String = "",
            @SerializedName("level")
            val level: String = ""
        )

        data class Destination(
            @SerializedName("info")
            val info: String = "",
            @SerializedName("label")
            val label: String = "",
            @SerializedName("phone")
            val phone: String = ""
        )

        data class DriverDetails(
            @SerializedName("karma")
            val karma: List<Karma> = listOf(),
            @SerializedName("licensePlateNumber")
            val licensePlateNumber: String = "",
            @SerializedName("name")
            val name: String = "",
            @SerializedName("photoUrl")
            val photoUrl: String = ""
        ) {
            data class Karma(
                @SerializedName("icon")
                val icon: String = "",
                @SerializedName("message")
                val message: String = "",
            )
        }

        data class Eta(
            @SerializedName("label")
            val label: String = "",
            @SerializedName("time")
            val time: String = ""
        )

        data class Invoice(
            @SerializedName("gofoodOrderNumber")
            val gofoodOrderNumber: String? = "",
            @SerializedName("invoiceNumber")
            val invoiceNumber: String = "",
            @SerializedName("invoiceURL")
            val invoiceURL: String = ""
        )

        @SuppressLint("Invalid Data Type")
        data class Item(
            @SerializedName("cartId")
            val cartId: String = "",
            @SerializedName("categoryId")
            val categoryId: String = "",
            @SerializedName("categoryName")
            val categoryName: String = "",
            @SerializedName("itemId")
            val itemId: String = "",
            @SerializedName("displayName")
            val displayName: String = "",
            @SerializedName("notes")
            val notes: String? = "",
            @SerializedName("price")
            val price: String = "",
            @SerializedName("quantity")
            val quantity: String = "",
            @SerializedName("variants")
            val variants: List<Variant>? = listOf()
        ) {
            data class Variant(
                @SerializedName("displayName")
                val displayName: String = "",
                @SerializedName("optionName")
                val optionName: String = ""
            )
        }

        data class Merchant(
            @SerializedName("merchantId")
            val merchantId: String = "",
            @SerializedName("displayName")
            val displayName: String = "",
            @SerializedName("distanceInKm")
            val distanceInKm: String = ""
        )

        data class OrderStatus(
            @SerializedName("iconName")
            val iconName: String? = "",
            @SerializedName("status")
            val status: String = "",
            @SerializedName("subtitle")
            val subtitle: String? = "",
            @SerializedName("title")
            val title: String = "",
            @SerializedName("enableChatButton")
            val isEnableChatButton: Boolean = true,
        )

        data class Payment(
            @SerializedName("paymentAmount")
            val paymentAmount: PaymentAmount = PaymentAmount(),
            @SerializedName("paymentDate")
            val paymentDate: String = "",
            @SerializedName("paymentDetails")
            val paymentDetails: List<PaymentDetail> = listOf(),
            @SerializedName("paymentMethod")
            val paymentMethod: PaymentMethod = PaymentMethod()
        ) {
            data class PaymentAmount(
                @SerializedName("label")
                val label: String = "",
                @SerializedName("value")
                val value: String = ""
            )

            data class PaymentDetail(
                @SerializedName("label")
                val label: String = "",
                @SerializedName("value")
                val value: String = ""
            )

            data class PaymentMethod(
                @SerializedName("label")
                val label: String = "",
                @SerializedName("value")
                val value: String = ""
            )
        }
    }
}
