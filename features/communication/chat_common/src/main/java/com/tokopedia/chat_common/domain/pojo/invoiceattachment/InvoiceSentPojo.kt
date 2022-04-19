package com.tokopedia.chat_common.domain.pojo.invoiceattachment

import com.google.gson.annotations.SerializedName

/**
 * @author by yfsx on 16/05/18.
 */

class InvoiceSentPojo {

    @SerializedName("invoice_link")
    var invoiceLink: InvoiceLinkPojo = InvoiceLinkPojo()

}
