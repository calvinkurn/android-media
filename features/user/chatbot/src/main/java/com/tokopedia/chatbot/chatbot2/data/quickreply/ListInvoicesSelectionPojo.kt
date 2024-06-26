package com.tokopedia.chatbot.chatbot2.data.quickreply

import com.google.gson.annotations.SerializedName
import com.tokopedia.chatbot.chatbot2.data.invoicelist.websocket.InvoicesSelectionPojo

/**
 * Created by Hendri on 16/05/18.
 */
class ListInvoicesSelectionPojo {
    @SerializedName("invoice_list")
    var invoices: InvoicesSelectionPojo = InvoicesSelectionPojo()

    @SerializedName("has_next")
    var hasNext: Boolean = false
}
