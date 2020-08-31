package com.tokopedia.payment.fingerprint.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by zulfikarrahman on 3/27/18.
 */
data class DataResponseSavePublicKey(
        @SerializedName("is_success")
        @Expose
        var success: Int = 0
)