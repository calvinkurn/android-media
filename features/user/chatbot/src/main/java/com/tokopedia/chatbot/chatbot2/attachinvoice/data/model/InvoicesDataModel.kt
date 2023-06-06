package com.tokopedia.chatbot.chatbot2.attachinvoice.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Hendri on 21/03/18.
 */

class InvoicesDataModel(
    @field:SerializedName("type_id")
    @field:Expose
    var typeId: Long,
    @field:SerializedName("type")
    @field:Expose
    var type: String = "",
    @field:SerializedName("attributes")
    @field:Expose
    var attribute: InvoiceAttributesDataModel?
)
