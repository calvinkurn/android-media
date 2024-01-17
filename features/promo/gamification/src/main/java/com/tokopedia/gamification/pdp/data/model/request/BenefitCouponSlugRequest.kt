package com.tokopedia.gamification.pdp.data.model.request

import com.google.gson.annotations.SerializedName

data class BenefitCouponSlugRequest(
    @SerializedName("serviceID")
    val serviceID: String,
    @SerializedName("categoryID")
    val categoryID: Long,
    @SerializedName("categoryIDCoupon")
    val categoryIDCoupon: Long,
    @SerializedName("page")
    val page: Long,
    @SerializedName("limit")
    val limit: Long,
    @SerializedName("includeExtraInfo")
    val includeExtraInfo: Int,
    @SerializedName("source")
    val source: String,
    @SerializedName("isGetPromoInfo")
    val isGetPromoInfo: Boolean,
    @SerializedName("apiVersion")
    val apiVersion: String?,
    @SerializedName("catalogSlugs")
    val catalogSlugs: List<String>?,
    @SerializedName("clientID")
    val clientID: String?
)

