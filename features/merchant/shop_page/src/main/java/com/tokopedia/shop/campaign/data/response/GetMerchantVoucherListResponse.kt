package com.tokopedia.shop.campaign.data.response


import com.google.gson.annotations.SerializedName

data class GetMerchantVoucherListResponse(
    @SerializedName("tokopointsCatalogMVCList")
    val tokopointsCatalogMVCList: TokopointsCatalogMVCList
) {
    data class TokopointsCatalogMVCList(
        @SerializedName("catalogList")
        val catalogList: List<Catalog>,
        @SerializedName("resultStatus")
        val resultStatus: ResultStatus
    ) {
        data class Catalog(
            @SerializedName("baseCode")
            val baseCode: String,
            @SerializedName("catalogType")
            val catalogType: Int,
            @SerializedName("eligibleProductIDs")
            val eligibleProductIDs: List<Any>,
            @SerializedName("eligibleProductIDsLabel")
            val eligibleProductIDsLabel: String,
            @SerializedName("expiredDate")
            val expiredDate: String,
            @SerializedName("expiredLabel")
            val expiredLabel: String,
            @SerializedName("id")
            val id: String,
            @SerializedName("maximumBenefitAmount")
            val maximumBenefitAmount: Long,
            @SerializedName("minimumUsageAmount")
            val minimumUsageAmount: Long,
            @SerializedName("minimumUsageLabel")
            val minimumUsageLabel: String,
            @SerializedName("promoID")
            val promoID: String,
            @SerializedName("promoType")
            val promoType: String,
            @SerializedName("quotaLeft")
            val quotaLeft: Int,
            @SerializedName("quotaLeftLabel")
            val quotaLeftLabel: String,
            @SerializedName("slug")
            val slug: String,
            @SerializedName("tagImageURLs")
            val tagImageURLs: List<Any>,
            @SerializedName("title")
            val title: String
        )

        data class ResultStatus(
            @SerializedName("code")
            val code: String,
            @SerializedName("message")
            val message: List<String>,
            @SerializedName("status")
            val status: String
        )
    }
}
