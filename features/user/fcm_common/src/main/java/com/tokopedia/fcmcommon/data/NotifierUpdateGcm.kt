package com.tokopedia.fcmcommon.data


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class NotifierUpdateGcm(
    @SerializedName("error_message")
    @Expose
    val errorMessage: String = "",
    @SerializedName("is_success")
    @Expose
    val isSuccess: Int = -1,
    @SerializedName("status")
    @Expose
    val status: Int = -1
)