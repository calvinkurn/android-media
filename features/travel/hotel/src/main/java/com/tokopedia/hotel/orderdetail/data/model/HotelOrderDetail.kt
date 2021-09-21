package com.tokopedia.hotel.orderdetail.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by jessica on 09/05/19
 */

data class HotelOrderDetail(
        @SerializedName("status")
        @Expose
        val status: Status = Status(),

        @SerializedName("title")
        @Expose
        val title: List<LabelValue> = listOf(),

        @SerializedName("invoice")
        @Expose
        val invoice: Invoice = Invoice(),

        @SerializedName("conditionalInfo")
        @Expose
        val conditionalInfo: ConditionalInfo = ConditionalInfo(),

        @SerializedName("conditionalInfoBottom")
        @Expose
        val conditionalInfoBottom: ConditionalInfo = ConditionalInfo(),

        @SerializedName("actionButtons")
        @Expose
        val actionButtons: List<ActionButton> = listOf(),

        @SerializedName("payMethod")
        @Expose
        val payMethod: List<LabelValue> = listOf(),

        @SerializedName("pricing")
        @Expose
        val pricing: List<PaymentData> = listOf(),

        @SerializedName("paymentsData")
        @Expose
        val paymentsData: List<PaymentData> = listOf(),

        @SerializedName("contactUs")
        @Expose
        val contactUs: Contact = Contact(),

        @SerializedName("hotelTransportDetails")
        @Expose
        val hotelTransportDetails: HotelTransportDetail = HotelTransportDetail()
) {
    data class Status(
            @SerializedName("status")
            @Expose
            val status: Int = 0,

            @SerializedName("statusText")
            @Expose
            val statusText: String = "",

            @SerializedName("iconUrl")
            @Expose
            val iconUrl: String = "",

            @SerializedName("textColor")
            @Expose
            val textColor: String = "",

            @SerializedName("backgroundColor")
            @Expose
            val backgroundColor: String = "",

            @SerializedName("fontSize")
            @Expose
            val fontsize: String = ""
    )

    data class LabelValue(
            @SerializedName("label")
            @Expose
            val label: String = "",

            @SerializedName("value")
            @Expose
            val value: String = ""
    )

    data class Invoice(
            @SerializedName("invoiceRefNum")
            @Expose
            val invoiceRefNum: String = "",

            @SerializedName("invoiceUrl")
            @Expose
            val invoiceUrl: String = ""
    )

    data class ConditionalInfo(
            @SerializedName("title")
            @Expose
            val title: String = ""
    )

    data class ActionButton(
            @SerializedName("label")
            @Expose
            val label: String = "",

            @SerializedName("buttonType")
            @Expose
            val buttonType: String = "",

            @SerializedName("uri")
            @Expose
            val uri: String = "",

            @SerializedName("uriWeb")
            @Expose
            val uriWeb: String = "",

            @SerializedName("weight")
            @Expose
            val weight: Int = 0
    )

    data class PaymentData(
            @SerializedName("label")
            @Expose
            val label: String = "",

            @SerializedName("value")
            @Expose
            val value: String = "",

            @SerializedName("textColor")
            @Expose
            val textColor: String = "",

            @SerializedName("backgroundColor")
            @Expose
            val backgroundColor: String = "",

            @SerializedName("imageUrl")
            @Expose
            val imageUrl: String = ""
    )

    data class Contact(
            @SerializedName("helpText")
            @Expose
            val helpText: String = "",

            @SerializedName("helpUrl")
            @Expose
            val helpUrl: String = ""
    )

    data class Response(
            @SerializedName("orderDetails")
            @Expose
            val response: HotelOrderDetail = HotelOrderDetail()
    )

}