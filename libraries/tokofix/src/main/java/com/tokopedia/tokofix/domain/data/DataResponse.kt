package com.tokopedia.tokofix.domain.data


import com.google.gson.annotations.SerializedName

data class DataResponse(
    @SerializedName("code")
    val code: Int = 0,
    @SerializedName("data")
    val `data`: Data = Data(),
    @SerializedName("message")
    val message: String = ""
) {
    data class Data(
        @SerializedName("description")
        val description: String = "",
        @SerializedName("download_url")
        val downloadUrl: String = "",
        @SerializedName("version")
        val version: String = ""
    )
}