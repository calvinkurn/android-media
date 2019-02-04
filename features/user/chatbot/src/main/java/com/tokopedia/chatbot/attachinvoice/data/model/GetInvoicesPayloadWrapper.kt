package com.tokopedia.chatbot.attachinvoice.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Hendri on 28/03/18.
 */

class GetInvoicesPayloadWrapper {

    @SerializedName("has_next")
    @Expose
    var isHasNext: Boolean = false

    @SerializedName("invoices")
    @Expose
    var invoices: List<InvoicesDataModel> = ArrayList()
}
