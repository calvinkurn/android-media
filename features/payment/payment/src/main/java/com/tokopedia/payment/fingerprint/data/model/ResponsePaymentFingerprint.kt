package com.tokopedia.payment.fingerprint.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by zulfikarrahman on 3/27/18.
 */
data class ResponsePaymentFingerprint(
        @SerializedName("success")
        @Expose
        var isSuccess: Boolean = false,

        @SerializedName("message")
        @Expose
        var message: String = "",

        @SerializedName("url")
        @Expose
        var url: String = "",

        @SerializedName("method")
        @Expose
        var method: String = "",

        @SerializedName("param_encode")
        @Expose
        var paramEncode: String = ""
)