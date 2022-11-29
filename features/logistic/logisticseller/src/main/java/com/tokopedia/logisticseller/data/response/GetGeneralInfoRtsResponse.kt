package com.tokopedia.logisticseller.data.response

import com.google.gson.annotations.SerializedName

data class GetGeneralInfoRtsResponse(
    @SerializedName("status")
    val status: Int = 0,
    @SerializedName("message_error")
    val messageError: String = "",
    @SerializedName("data")
    val data: GeneralInfoRtsData? = null,
) {
    data class GeneralInfoRtsData(
        @SerializedName("title")
        val title: String = "",
        @SerializedName("description")
        val description: String = "",
        @SerializedName("invoice")
        val invoice: String = "",
        @SerializedName("image")
        val image: Image = Image(),
        @SerializedName("article_url")
        val articleUrl: String = "",
    ) {
        val urlImage: String
            get() = BASE_URL_IMAGE + image.imageId
    }

    data class Image(
        @SerializedName("image_id")
        val imageId: String = "",
    )

    val isSuccess: Boolean
        get() = status == STATUS_SUCCESS

    companion object {
        private const val STATUS_SUCCESS = 200
        private const val BASE_URL_IMAGE = ""
    }
}
