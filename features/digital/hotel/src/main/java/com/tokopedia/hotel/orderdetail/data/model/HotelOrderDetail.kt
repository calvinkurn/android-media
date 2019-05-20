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
        val title: List<Title> = listOf(),

        @SerializedName("invoice")
        @Expose
        val invoice: Invoice = Invoice(),

        @SerializedName("contactUs")
        @Expose
        val contactUs: Contact = Contact(),

        @SerializedName("actionButtons")
        @Expose
        val actionButtons: List<ActionButton> = listOf(),

        @SerializedName("hotelTransportDetails")
        @Expose
        val hotelTransportDetails: List<HotelTransportDetail> = listOf()
) {
    data class Status(
            @SerializedName("status")
            @Expose
            val status: Int = 0,

            @SerializedName("statusText")
            @Expose
            val statusText: String = ""
    )

    data class Title(
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

    data class Contact(
            @SerializedName("helpText")
            @Expose
            val helpText: String = "",

            @SerializedName("helpUrl")
            @Expose
            val helpUrl: String = ""
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

    data class Response(
            @SerializedName("orderDetails")
            @Expose
            val response: HotelOrderDetail = HotelOrderDetail()
    )

}