package com.tokopedia.logisticseller.data.response

import com.google.gson.annotations.SerializedName

data class GetGeneralInfoRtsResponse(
    @SerializedName("getGeneralInformation")
    val generalInformation: GetGeneralInfoRts = GetGeneralInfoRts()
) {
    data class GetGeneralInfoRts(
        @SerializedName("status")
        val status: Int = 0,
        @SerializedName("message_error")
        val messageError: String = "",
        @SerializedName("data")
        val data: GeneralInfoRtsData? = null
    )

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
        val articleUrl: String = ""
    )

    data class Image(
        @SerializedName("image_id")
        val imageId: String? = null,
        @SerializedName("copy_writing_disclaimer")
        val imageDisclaimer: String? = null
    ) {
        var urlImage: String = ""
        var accessToken: String = ""
    }

    val messageError: String
        get() = generalInformation.messageError

    val isSuccess: Boolean
        get() = generalInformation.status == STATUS_SUCCESS

    companion object {
        private const val STATUS_SUCCESS = 200
    }
}
