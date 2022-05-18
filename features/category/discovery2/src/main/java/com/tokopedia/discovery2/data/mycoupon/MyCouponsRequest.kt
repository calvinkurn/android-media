package com.tokopedia.discovery2.data.mycoupon

import com.google.gson.annotations.SerializedName

data class MyCouponsRequest(
        @SerializedName("categoryID")
        var categoryID: Int,

        @SerializedName("page")
        var page: Int,

        @SerializedName("limit")
        var limit: Int,

        @SerializedName("serviceID")
        var serviceID: String,

        @SerializedName("categoryIDCoupon")
        var categoryIDCoupon: Int,

        @SerializedName("includeExtraInfo")
        var includeExtraInfo: Int,

        @SerializedName("apiVersion")
        var apiVersion: String = "",

        @SerializedName("source")
        var source: String = "",

        @SerializedName("catalogSlugs")
        var catalogSlugs: List<String?>,

        @SerializedName("clientID")
        var clientID: String = "",

        @SerializedName("isGetPromoInfo")
        var isGetPromoInfo: Boolean


)