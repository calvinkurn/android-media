package com.tokopedia.chatbot.domain.pojo

import com.google.gson.annotations.SerializedName

/**
 * @author by yfsx on 16/05/18.
 */
class InvoiceLinkPojo {

    @SerializedName("attributes")
    var attributes: InvoiceLinkAttributePojo = InvoiceLinkAttributePojo()
    @SerializedName("type")
    var type: String? = null
    @SerializedName("type_id")
    var typeId: Int = 0

}
