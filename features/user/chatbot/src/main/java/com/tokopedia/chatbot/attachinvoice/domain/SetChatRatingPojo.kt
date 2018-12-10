package com.tokopedia.chatbot.attachinvoice.domain

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import java.util.ArrayList

/**
 * @author by alvinatin on 26/03/18.
 */

class SetChatRatingPojo {
    @SerializedName("message")
    @Expose
    val message: String? = null
    @SerializedName("is_success")
    @Expose
    val isSuccess: Boolean = false
    @SerializedName("reasons")
    @Expose
    val reasons: ArrayList<String>? = null
}
