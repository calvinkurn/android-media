package com.tokopedia.shop.campaign.data.response


import com.google.gson.annotations.SerializedName


data class GetPromoVoucherListMapperResponse(
    @SerializedName("tokopointsCatalogWithCouponList")
    val tokopointsCatalogWithCouponList: TokopointsCatalogWithCouponList
) {
    data class TokopointsCatalogWithCouponList(
        @SerializedName("catalogWithCouponList")
        val catalogWithCouponList: List<CatalogWithCoupon>,
        @SerializedName("resultStatus")
        val resultStatus: ResultStatus
    ) {
        data class CatalogWithCoupon(
            @SerializedName("appLink")
            val appLink: String,
            @SerializedName("baseCode")
            val baseCode: String,
            @SerializedName("buttonStr")
            val buttonStr: String,
            @SerializedName("catalogType")
            val catalogType: Int,
            @SerializedName("couponCode")
            val couponCode: String,
            @SerializedName("cta")
            val cta: String,
            @SerializedName("ctaDesktop")
            val ctaDesktop: String,
            @SerializedName("disableErrorMessage")
            val disableErrorMessage: String,
            @SerializedName("id")
            val id: Long,
            @SerializedName("imageUrl")
            val imageUrl: String,
            @SerializedName("imageUrlMobile")
            val imageUrlMobile: String,
            @SerializedName("isDisabled")
            val isDisabled: Boolean,
            @SerializedName("isDisabledButton")
            val isDisabledButton: Boolean,
            @SerializedName("minimumUsage")
            val minimumUsage: String,
            @SerializedName("minimumUsageLabel")
            val minimumUsageLabel: String,
            @SerializedName("promoID")
            val promoID: Int,
            @SerializedName("quota")
            val quota: Int,
            @SerializedName("slug")
            val slug: String,
            @SerializedName("smallImageUrl")
            val smallImageUrl: String,
            @SerializedName("smallImageUrlMobile")
            val smallImageUrlMobile: String,
            @SerializedName("subTitle")
            val subTitle: String,
            @SerializedName("thumbnailUrl")
            val thumbnailUrl: String,
            @SerializedName("thumbnailUrlMobile")
            val thumbnailUrlMobile: String,
            @SerializedName("title")
            val title: String,
            @SerializedName("upperTextDesc")
            val upperTextDesc: List<String>,
            @SerializedName("url")
            val url: String
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

