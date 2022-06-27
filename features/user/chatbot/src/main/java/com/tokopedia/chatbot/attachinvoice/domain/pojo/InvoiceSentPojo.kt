package com.tokopedia.chatbot.attachinvoice.domain.pojo

import com.google.gson.annotations.SerializedName

class InvoiceSentPojo {

    @SerializedName("invoice_link")
    var invoiceLink: InvoiceLinkPojo = InvoiceLinkPojo()

}