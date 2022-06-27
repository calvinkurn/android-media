package com.tokopedia.chatbot.attachinvoice.domain.pojo

import com.google.gson.annotations.SerializedName

data class InvoiceLinkPojo (
    @SerializedName("attributes")
    var attributes: InvoiceLinkAttributePojo = InvoiceLinkAttributePojo(),

    @SerializedName("type")
    var type: String? = null,

    @SerializedName("type_id")
    var typeId: Long = 0
)

