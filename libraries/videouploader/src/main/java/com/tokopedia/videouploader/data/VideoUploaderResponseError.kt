package com.tokopedia.videouploader.data

import android.text.TextUtils
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.common.data.model.response.BaseResponseError
import com.tokopedia.abstraction.common.data.model.response.ResponseV4ErrorException
import java.io.IOException

/**
 * @author by nisie on 14/03/19.
 */
open class VideoUploaderResponseError(
        @SerializedName("message_error")
        @Expose
        private val messageError: List<String> = ArrayList(),
        @SerializedName("status")
        @Expose
        private val status: String = "") : BaseResponseError() {

    private val STATUS_OK = "OK"


    override fun isResponseErrorValid(): Boolean {
        if (TextUtils.isEmpty(status)) {
            return false
        }
        return if (status.equals(STATUS_OK, ignoreCase = true)) {
            false
        } else hasBody()
    }

    override fun getErrorKey(): String {
        return "message_error"
    }

    override fun hasBody(): Boolean {
        return messageError.isNotEmpty()
    }

    override fun createException(): IOException {
        return ResponseV4ErrorException(messageError)
    }


}