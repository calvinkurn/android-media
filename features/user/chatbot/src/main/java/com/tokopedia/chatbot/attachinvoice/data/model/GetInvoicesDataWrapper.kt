package com.tokopedia.chatbot.attachinvoice.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Hendri on 28/03/18.
 */

class GetInvoicesDataWrapper {
    @SerializedName("payload")
    @Expose
    var payloadWrapper: GetInvoicesPayloadWrapper? = null
}
