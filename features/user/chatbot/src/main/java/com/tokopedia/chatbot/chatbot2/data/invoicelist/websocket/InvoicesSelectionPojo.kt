package com.tokopedia.chatbot.chatbot2.data.invoicelist.websocket

import com.google.gson.annotations.SerializedName

/**
 * Created by Hendri on 16/05/18.
 */
class InvoicesSelectionPojo {
    @SerializedName("invoices")
    var invoices: List<InvoicesSelectionSingleItemPojo> = ArrayList()
}
