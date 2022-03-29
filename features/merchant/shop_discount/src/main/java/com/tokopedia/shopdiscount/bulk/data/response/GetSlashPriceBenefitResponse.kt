package com.tokopedia.shopdiscount.bulk.data.response


import com.google.gson.annotations.SerializedName

data class GetSlashPriceBenefitResponse(
    @SerializedName("GetSlashPriceBenefit")
    val getSlashPriceBenefit: GetSlashPriceBenefit = GetSlashPriceBenefit()
) {
    data class GetSlashPriceBenefit(
        @SerializedName("is_use_vps")
        val isUseVps: Boolean = false,
        @SerializedName("response_header")
        val responseHeader: ResponseHeader = ResponseHeader(),
        @SerializedName("slash_price_benefits")
        val slashPriceBenefits: List<SlashPriceBenefit> = listOf()
    ) {
        data class ResponseHeader(
            @SerializedName("error_code")
            val errorCode: String = "",
            @SerializedName("process_time")
            val processTime: Double = 0.0,
            @SerializedName("reason")
            val reason: String = "",
            @SerializedName("status")
            val status: String = "",
            @SerializedName("success")
            val success: Boolean = false
        )

        data class SlashPriceBenefit(
            @SerializedName("expired_at")
            val expiredAt: String = "",
            @SerializedName("expired_at_unix")
            val expiredAtUnix: Int = 0,
            @SerializedName("max_quota")
            val maxQuota: Int = 0,
            @SerializedName("package_id")
            val packageId: String = "",
            @SerializedName("package_name")
            val packageName: String = "",
            @SerializedName("remaining_quota")
            val remainingQuota: Int = 0,
            @SerializedName("shop_grade")
            val shopGrade: Int = 0,
            @SerializedName("shop_tier")
            val shopTier: Int = 0
        )
    }
}