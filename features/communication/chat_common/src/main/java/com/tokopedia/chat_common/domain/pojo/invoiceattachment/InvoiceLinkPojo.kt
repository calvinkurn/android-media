package com.tokopedia.chat_common.domain.pojo.invoiceattachment

import com.google.gson.annotations.SerializedName

/**
 * @author by yfsx on 16/05/18.
 */
data class InvoiceLinkPojo (
    @SerializedName("attributes")
    var attributes: InvoiceLinkAttributePojo = InvoiceLinkAttributePojo(),

    @SerializedName("type")
    var type: String? = null,

    @SerializedName("type_id")
    var typeId: String = "0"
)
