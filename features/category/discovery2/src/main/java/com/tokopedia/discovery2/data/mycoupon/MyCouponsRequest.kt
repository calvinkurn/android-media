package com.tokopedia.discovery2.data.mycoupon

import com.google.gson.annotations.SerializedName

data class MyCouponsRequest(
        @SerializedName("categoryID")
        var categoryID: Int? = null,

        @SerializedName("page")
        var page: Int? = null,

        @SerializedName("limit")
        var limit: Int? = null,

        @SerializedName("serviceID")
        var serviceID: String = "",

        @SerializedName("categoryIDCoupon")
        var categoryIDCoupon: Int? = null,

        @SerializedName("includeExtraInfo")
        var includeExtraInfo: Int? = null,

        @SerializedName("apiVersion")
        var apiVersion: String? = "",

        @SerializedName("source")
        var source: String? = "",

        @SerializedName("catalogSlugs")
        var catalogSlugs: List<String?>? = null,

        @SerializedName("clientID")
        var clientID: String? = "",

        @SerializedName("isGetPromoInfo")
        var isGetPromoInfo: Boolean? = null


)