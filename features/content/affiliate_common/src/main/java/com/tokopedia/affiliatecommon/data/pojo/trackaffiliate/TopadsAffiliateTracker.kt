package com.tokopedia.affiliatecommon.data.pojo.trackaffiliate


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TopadsAffiliateTracker(
        @SerializedName("errors")
        @Expose
        val errors: List<Any> = listOf(),
        @SerializedName("message")
        @Expose
        val message: String? = "",
        @SerializedName("success")
        @Expose
        val success: String? = ""
) {
        val isSuccess: Boolean
                get() = success?.toLowerCase() == "true"
}