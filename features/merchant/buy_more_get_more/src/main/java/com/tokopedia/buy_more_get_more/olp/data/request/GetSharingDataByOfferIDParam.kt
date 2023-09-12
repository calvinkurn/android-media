package com.tokopedia.buy_more_get_more.olp.data.request

import com.google.gson.annotations.SerializedName
import com.tokopedia.config.GlobalConfig

data class GetSharingDataByOfferIDParam(
    @SerializedName("request_header")
    val requestHeader: RequestHeader = RequestHeader(),
    @SerializedName("offer_id")
    val offerId: Long = 0,
    @SerializedName("shop_id")
    val shopId: Long = 0
) {
    data class RequestHeader(
        @SerializedName("source")
        val source: String = "shop",
        @SerializedName("usecase")
        val useCase: String = "offer landing page",
        @SerializedName("version")
        val version: String = GlobalConfig.VERSION_NAME,
        @SerializedName("device")
        val device: String = "android"
    )
}
