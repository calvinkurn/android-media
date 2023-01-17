package com.tokopedia.chatbot.attachinvoice.domain.pojo

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class InvoiceSentPojo(

    @SerializedName("invoice_link")
    var invoiceLink: InvoiceLinkPojo = InvoiceLinkPojo()

)

data class InvoiceLinkPojo(
    @SerializedName("attributes")
    var attributes: InvoiceLinkAttributePojo = InvoiceLinkAttributePojo(),

    @SerializedName("type")
    var type: String? = null,

    @SerializedName("type_id")
    var typeId: Long = 0
)

data class InvoiceLinkAttributePojo(

    @SerializedName("code")
    var code: String = "",

    @SerializedName("create_time")
    var createTime: String = "",

    @SerializedName("description")
    var description: String = "",

    @SerializedName("href_url")
    var hrefUrl: String = "",

    @SerializedName("id")
    var id: Long = 0,

    @SerializedName("image_url")
    var imageUrl: String = "",

    @SerializedName("status")
    var status: String = "",

    @SuppressLint("Invalid Data Type")
    @SerializedName("status_id")
    var statusId: Int = 0,

    @SerializedName("title")
    var title: String = "",

    @SerializedName("total_amount")
    var totalAmount: String = "",

    @SerializedName("color")
    var color: String = ""
)

