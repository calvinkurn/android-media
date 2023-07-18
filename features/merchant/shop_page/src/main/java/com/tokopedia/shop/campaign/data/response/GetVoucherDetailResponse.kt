package com.tokopedia.shop.campaign.data.response


import com.google.gson.annotations.SerializedName

data class GetVoucherDetailResponse(
    @SerializedName("hachikoCatalogDetail")
    val hachikoCatalogDetail: HackhikoCatalogDetail
) {
    data class HackhikoCatalogDetail(
        @SerializedName("activePeriod")
        val activePeriod: String,
        @SerializedName("activePeriodDate")
        val activePeriodDate: String,
        @SerializedName("button_str")
        val buttonStr: String,
        @SerializedName("catalog_type")
        val catalogType: Int,
        @SerializedName("cta")
        val cta: String,
        @SerializedName("disable_error_message")
        val disableErrorMessage: String,
        @SerializedName("discount_percentage")
        val discountPercentage: Int,
        @SerializedName("discount_percentage_str")
        val discountPercentageStr: String,
        @SerializedName("expired")
        val expired: String,
        @SerializedName("expired_label")
        val expiredLabel: String,
        @SerializedName("expired_str")
        val expiredStr: String,
        @SerializedName("how_to_use")
        val howToUse: String,
        @SerializedName("id")
        val id: Long,
        @SerializedName("image_url")
        val imageUrl: String,
        @SerializedName("image_url_mobile")
        val imageUrlMobile: String,
        @SerializedName("is_disabled")
        val isDisabled: Boolean,
        @SerializedName("is_disabled_button")
        val isDisabledButton: Boolean,
        @SerializedName("is_gift")
        val isGift: Int,
        @SerializedName("minimumUsage")
        val minimumUsage: String,
        @SerializedName("minimumUsageLabel")
        val minimumUsageLabel: String,
        @SerializedName("overview")
        val overview: String,
        @SerializedName("points")
        val points: Int,
        @SerializedName("points_slash")
        val pointsSlash: Int,
        @SerializedName("points_slash_str")
        val pointsSlashStr: String,
        @SerializedName("points_str")
        val pointsStr: String,
        @SerializedName("quota")
        val quota: Int,
        @SerializedName("sub_title")
        val subTitle: String,
        @SerializedName("thumbnail_url")
        val thumbnailUrl: String,
        @SerializedName("thumbnail_url_mobile")
        val thumbnailUrlMobile: String,
        @SerializedName("title")
        val title: String,
        @SerializedName("tnc")
        val tnc: String,
        @SerializedName("upper_text_desc")
        val upperTextDesc: List<String>
    )
}
