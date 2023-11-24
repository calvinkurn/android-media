package com.tokopedia.mediauploader.video.data.entity

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("error_message") val message: String? = "",
    @SerializedName("header") val header: Header? = null
) {

    data class Header(
        @SerializedName("is_success") val isSuccess: Boolean? = false,
        @SerializedName("req_id") val requestId: String? = ""
    )
}
