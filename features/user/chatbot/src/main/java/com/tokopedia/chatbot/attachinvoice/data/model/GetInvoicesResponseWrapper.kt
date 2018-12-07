package com.tokopedia.chatbot.attachinvoice.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Hendri on 28/03/18.
 */

class GetInvoicesResponseWrapper {
    @SerializedName("data")
    @Expose
    var dataWrapper: GetInvoicesDataWrapper? = null
}
