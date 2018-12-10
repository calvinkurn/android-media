package com.tokopedia.chatbot.attachinvoice.domain

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by nisie on 6/11/18.
 */
class SendReasonRatingPojo {
    @SerializedName("is_success")
    @Expose
    val isSuccess: Boolean = false
}
