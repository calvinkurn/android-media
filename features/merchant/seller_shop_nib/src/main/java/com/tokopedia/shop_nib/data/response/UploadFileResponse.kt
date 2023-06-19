package com.tokopedia.shop_nib.data.response


import com.google.gson.annotations.SerializedName

data class UploadFileResponse(
    @SerializedName("header")
    val header: Header = Header()
) {
    data class Header(
        @SerializedName("error_code")
        val errorCode: String = "",
        @SerializedName("messages")
        val messages: List<String> = emptyList(),
        @SerializedName("process_time")
        val processTime: Double = 0.0,
        @SerializedName("reason")
        val reason: String = ""
    )
}
