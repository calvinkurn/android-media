package com.tokopedia.discovery2.data.claim_coupon

import com.google.gson.annotations.SerializedName

data class ClaimCouponRequest(
        @SerializedName("catalogSlugs")
        var catalogSlugs: List<String>,

        @SerializedName("categorySlug")
        var categorySlug: String = ""

)
