package com.tokopedia.buyerorder.detail.data

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose
import com.tokopedia.buyerorder.recharge.data.response.AdditionalTickerInfo

/**
 * Created by baghira on 10/05/18.
 */
data class OrderDetails(
    @SerializedName("status")
    @Expose
    val status: Status = Status(),

    @SerializedName("conditionalInfo")
    @Expose
    val conditionalInfo: ConditionalInfo = ConditionalInfo(),

    @SerializedName("flag")
    var flags: Flags = Flags(),

    @SerializedName("title")
    @Expose
    val title: List<Title> = emptyList(),

    @SerializedName("invoice")
    @Expose
    val invoice: Invoice = Invoice(),

    @SerializedName("detail")
    @Expose
    val detail: List<Detail> = emptyList(),

    @SerializedName("additionalInfo")
    @Expose
    val additionalInfo: List<AdditionalInfo> = emptyList(),

    @SerializedName("additionalTickerInfo")
    @Expose
    val additionalTickerInfo: List<AdditionalTickerInfo> = emptyList(),

    @SerializedName("pricing")
    @Expose
    val pricing: List<Pricing> = emptyList(),

    @SerializedName("payMethod")
    @Expose
    val payMethods: List<PayMethod> = emptyList(),

    @SerializedName("paymentData")
    @Expose
    val paymentData: PaymentData = PaymentData(),

    @SerializedName("contactUs")
    @Expose
    val contactUs: ContactUs = ContactUs(),

    @SerializedName("actionButtons")
    @Expose
    val actionButtons: List<ActionButton> = emptyList(),

    @SerializedName("items")
    @Expose
    val items: List<Items> = emptyList(),

    @SerializedName("helpLink")
    @Expose
    val helpLink: String = "",

    @SerializedName("metadata")
    @Expose
    val metadata: String = ""

)