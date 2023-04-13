package com.tokopedia.shop_nib.data.response


import com.google.gson.annotations.SerializedName

data class UploadFileResponse(
    @SerializedName("data")
    val `data`: Data? = null
) {
    data class Data(
        @SerializedName("resultStatus")
        val resultStatus: ResultStatus? = null
    ) {
        data class ResultStatus(
            @SerializedName("code")
            val code: String? = null,
            @SerializedName("message")
            val message: List<String>? = null,
            @SerializedName("status")
            val status: String? = null
        )
    }
}
