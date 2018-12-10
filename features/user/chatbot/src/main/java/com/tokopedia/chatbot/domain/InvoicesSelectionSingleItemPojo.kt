package com.tokopedia.chatbot.domain

import com.google.gson.annotations.SerializedName

/**
 * Created by Hendri on 16/05/18.
 */
class InvoicesSelectionSingleItemPojo {

    @SerializedName("attributes")
    var attributes: InvoiceSingleItemAttributes? = null
    @SerializedName("type")
    var type: String? = null
    @SerializedName("type_id")
    var typeId: Int = 0
}
