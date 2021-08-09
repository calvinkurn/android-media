package com.tokopedia.loginfingerprint.data.model

import com.google.gson.annotations.SerializedName

data class RemoveFingerprintPojo(
    @SerializedName("flushFingerprintByUniqueID")
    var data: RemoveFingerprintData = RemoveFingerprintData()
)

data class RemoveFingerprintData(
    @SerializedName("is_success")
    var isSuccess: Boolean = false,

    @SerializedName("error")
    var error: String = "",

    @SerializedName("message")
    var message: String = ""
)