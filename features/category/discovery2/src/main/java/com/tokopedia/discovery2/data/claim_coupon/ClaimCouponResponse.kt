package com.tokopedia.discovery2.data.claim_coupon

import com.google.gson.annotations.SerializedName

data class ClaimCouponResponse(
    @SerializedName("tokopointsCatalogWithCouponList")
    val tokopointsCatalogWithCouponList: TokopointsCatalogWithCouponList? = null
)

data class TokopointsCatalogWithCouponList(
    @SerializedName("resultStatus")
    val resultStatus: ResultStatus? = null,
    @SerializedName("catalogWithCouponList")
    val catalogWithCouponList: List<CatalogWithCouponList>? = null,
    @SerializedName("countdownInfo")
    val countdownInfo: CountdownInfo? = null
)

data class CatalogWithCouponList(
    @SerializedName("id")
    val id: Long? = null,
    @SerializedName("promoID")
    val promoID: Long? = null,
    @SerializedName("quota")
    val quota: Long? = null,
    @SerializedName("title")
    val title: String? = "",
    @SerializedName("subTitle")
    val subTitle: String? = "",
    @SerializedName("isDisabled")
    val isDisabled: Boolean? = null,
    @SerializedName("disableErrorMessage")
    val disableErrorMessage: String? = "",
    @SerializedName("thumbnailUrl")
    val thumbnailURL: String? = "",
    @SerializedName("thumbnailUrlMobile")
    val thumbnailURLMobile: String? = "",
    @SerializedName("imageUrl")
    val imageURL: String? = "",
    @SerializedName("imageUrlMobile")
    val imageURLMobile: String? = "",
    @SerializedName("slug")
    val slug: String? = "",
    @SerializedName("baseCode")
    val baseCode: String? = "",
    @SerializedName("upperTextDesc")
    val upperTextDesc: List<String>? = null,
    @SerializedName("isDisabledButton")
    val isDisabledButton: Boolean? = null,
    @SerializedName("catalogType")
    val catalogType: Long? = null,
    @SerializedName("couponCode")
    val couponCode: String? = null,
    @SerializedName("cta")
    val cta: String? = "",
    @SerializedName("minimumUsageLabel")
    val minimumUsageLabel: String? = "",
    @SerializedName("minimumUsage")
    val minimumUsage: String? = "",
    @SerializedName("smallImageUrl")
    val smallImageURL: String? = "",
    @SerializedName("smallImageURLMobile")
    val smallImageURLMobile: String? = "",
    @SerializedName("buttonStr")
    val buttonStr: String? = null,
    @SerializedName("url")
    val url: String? = null,
    @SerializedName("appLink")
    var appLink: String? = "",

    var status: String? = null
)

data class CountdownInfo(
    @SerializedName("isShown")
    val isShown: Boolean? = null,
    @SerializedName("type")
    val type: Long? = null,
    @SerializedName("label")
    val label: String? = "",
    @SerializedName("countdownUnix")
    val countdownUnix: Long? = null,
    @SerializedName("countdownStr")
    val countdownStr: String? = "",
    @SerializedName("textColor")
    val textColor: String? = "",
    @SerializedName("backgroundColor")
    val backgroundColor: String? = ""
)

data class ResultStatus(
    @SerializedName("code")
    val code: String? = "",
    @SerializedName("status")
    val status: String? = "",
    @SerializedName("message")
    val message: List<String>? = null
)
