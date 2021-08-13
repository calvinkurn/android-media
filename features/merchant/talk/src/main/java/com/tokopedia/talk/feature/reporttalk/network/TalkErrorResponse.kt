package com.tokopedia.talk.feature.reporttalk.network

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.network.data.model.response.BaseResponseError
import com.tokopedia.network.exception.MessageErrorException
import java.io.IOException

/**
 * @author by nisie on 9/13/18.
 */
class TalkErrorResponse : BaseResponseError() {

    @SerializedName("message_error")
    @Expose
    private val messageError: List<String>? = null

    override fun getErrorKey(): String {
        return ""
    }

    override fun hasBody(): Boolean {
        return messageError != null && !messageError.isEmpty()
    }

    override fun createException(): IOException {
        return MessageErrorException(messageError!![0])
    }
}
