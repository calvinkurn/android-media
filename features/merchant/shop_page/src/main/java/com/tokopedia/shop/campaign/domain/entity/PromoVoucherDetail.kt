package com.tokopedia.shop.campaign.domain.entity

import com.google.gson.annotations.SerializedName

data class PromoVoucherDetail(
    @SerializedName("activePeriodDate")
    val activePeriodDate: String,
    @SerializedName("button_str")
    val buttonStr: String,
    @SerializedName("cta")
    val cta: String,
    @SerializedName("how_to_use")
    val howToUse: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("image_url")
    val imageUrl: String,
    @SerializedName("image_url_mobile")
    val imageUrlMobile: String,
    @SerializedName("is_disabled")
    val isDisabled: Boolean,
    @SerializedName("is_disabled_button")
    val isDisabledButton: Boolean,
    @SerializedName("minimumUsage")
    val minimumUsage: String,
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
    val tnc: String
)
