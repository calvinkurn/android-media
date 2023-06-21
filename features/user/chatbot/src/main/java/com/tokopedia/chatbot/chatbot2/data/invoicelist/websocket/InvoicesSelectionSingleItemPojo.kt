package com.tokopedia.chatbot.chatbot2.data.invoicelist.websocket

import com.google.gson.annotations.SerializedName

/**
 * Created by Hendri on 16/05/18.
 */
class InvoicesSelectionSingleItemPojo {

    @SerializedName("attributes")
    var attributes: InvoiceSingleItemAttributes = InvoiceSingleItemAttributes()

    @SerializedName("type")
    var type: String = ""

    @SerializedName("type_id")
    var typeId: Long = 0
}
