package com.tokopedia.centralizedpromo.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetPromotionListResponseWrapper(
        @Expose
        @SerializedName("MerchantPromotionGetPromotionList")
        val response: GetPromotionListResponse?
)

data class GetPromotionListResponse(
        @Expose
        @SerializedName("header")
        val header: Header?,
        @Expose
        @SerializedName("data")
        val data: GetPromotionListData?
)

data class GetPromotionListData(
        @Expose
        @SerializedName("title")
        val title: String?,
        @Expose
        @SerializedName("list")
        val promotions: List<Promotion>?
)

data class Promotion(
        @Expose
        @SerializedName("title")
        val title: String?,
        @Expose
        @SerializedName("status")
        val status: Status?,
        @Expose
        @SerializedName("footer")
        val footer: Footer?
)

data class Status(
        @Expose
        @SerializedName("text")
        val text: String?,
        @Expose
        @SerializedName("count")
        val count: Int?,
        @Expose
        @SerializedName("mobile_url")
        val url: String?
)

data class Footer(
        @Expose
        @SerializedName("text")
        val text: String?,
        @Expose
        @SerializedName("mobile_url")
        val url: String?
)

data class Header(
        @Expose
        @SerializedName("process_time")
        val processTime: Double?,
        @Expose
        @SerializedName("message")
        val message: List<String>?,
        @Expose
        @SerializedName("reason")
        val reason: String?,
        @Expose
        @SerializedName("error_code")
        val errorCode: String?
)
