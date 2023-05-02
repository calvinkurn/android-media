package com.tokopedia.shop_nib.data.response


import com.google.gson.annotations.SerializedName

data class UploadFileResponse(
    @SerializedName("data")
    val data: Data = Data(),
    @SerializedName("header")
    val header: Header = Header()
) {
    data class Data(
        @SerializedName("result_status")
        val resultStatus: ResultStatus = ResultStatus(),
        @SerializedName("status")
        val status: String = ""
    ) {
        data class ResultStatus(
            @SerializedName("code")
            val code: String = "",
            @SerializedName("message")
            val message: List<String> = emptyList()
        )
    }

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
