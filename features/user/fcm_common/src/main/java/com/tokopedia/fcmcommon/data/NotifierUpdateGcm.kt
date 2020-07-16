package com.tokopedia.fcmcommon.data


import com.google.gson.annotations.SerializedName

data class NotifierUpdateGcm(
    @SerializedName("error_message")
    val errorMessage: String = "",
    @SerializedName("is_success")
    val isSuccess: Int = -1,
    @SerializedName("status")
    val status: Int = -1
)