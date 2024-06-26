package com.tokopedia.shopdiscount.bulk.data.response


import com.google.gson.annotations.SerializedName
import com.tokopedia.shopdiscount.common.data.response.ResponseHeader

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
        data class SlashPriceBenefit(
            @SerializedName("expired_at")
            val expiredAt: String = "",
            @SerializedName("expired_at_unix")
            val expiredAtUnix: Long = 0,
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