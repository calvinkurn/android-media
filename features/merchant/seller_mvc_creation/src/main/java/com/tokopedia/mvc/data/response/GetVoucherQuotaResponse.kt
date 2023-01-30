package com.tokopedia.mvc.data.response

import com.google.gson.annotations.SerializedName

class GetVoucherQuotaResponse (
    @SerializedName("MerchantPromotionGetQuotaUsage" )
    val merchantPromotionGetQuotaUsage: MerchantPromotionGetQuotaUsage = MerchantPromotionGetQuotaUsage()
)

data class Header (
    @SerializedName("process_time")
    val processTime: Double = 0.toDouble(),
    @SerializedName("message")
    val message: ArrayList<String> = arrayListOf(),
    @SerializedName("reason")
    val reason: String = "",
    @SerializedName("error_code")
    val errorCode: String = ""
)

data class Quota (
    @SerializedName("used")
    val used: Int = 0,
    @SerializedName("remaining")
    val remaining: Int = 0,
    @SerializedName("total")
    val total: Int = 0
)

data class Sources (
    @SerializedName("name")
    val name: String = "",
    @SerializedName("used")
    val used: Int = 0,
    @SerializedName("remaining")
    val remaining: Int = 0,
    @SerializedName("total")
    val total: Int = 0,
    @SerializedName("expired")
    val expired: String = ""
)

data class Ticker (
    @SerializedName("title" ) val title : String = ""
)

data class Data (
    @SerializedName("quota")
    val quota: Quota = Quota(),
    @SerializedName("status_source")
    val statusSource: String = "",
    @SerializedName("sources")
    val sources: ArrayList<Sources> = arrayListOf(),
    @SerializedName("ticker")
    val ticker: Ticker = Ticker(),
    @SerializedName("cta_text")
    val ctaText: String = "",
    @SerializedName("cta_link")
    val ctaLink: String = ""
)

data class MerchantPromotionGetQuotaUsage (
    @SerializedName("header")
    val header: Header = Header(),
    @SerializedName("data")
    val data: Data = Data()
)
