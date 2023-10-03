package com.tokopedia.buy_more_get_more.olp.data.response

import com.google.gson.annotations.SerializedName

data class SharingDataByOfferIDResponse(
    @SerializedName("GetSharingDataByOfferID")
    val sharingDataByOfferId: SharingDataByOfferID = SharingDataByOfferID()
) {
    data class SharingDataByOfferID(
        @SerializedName("response_header")
        val responseHeader: ResponseHeader = ResponseHeader(),
        @SerializedName("offer_data")
        val offerData: OfferData = OfferData()
    )

    data class ResponseHeader(
        @SerializedName("success")
        val success: Boolean = true,
        @SerializedName("errorCode")
        val errorCode: Long = 0,
        @SerializedName("processTime")
        val processTime: String = ""
    )

    data class OfferData(
        @SerializedName("image_url")
        val imageUrl: String = "",
        @SerializedName("title")
        val title: String = "",
        @SerializedName("description")
        val description: String = "",
        @SerializedName("deep_link")
        val deeplink: String = "",
        @SerializedName("tag")
        val tag: String = "",
        @SerializedName("page_type")
        val pageType: String = "",
        @SerializedName("campaign_name")
        val campaignName: String = ""
    )
}
