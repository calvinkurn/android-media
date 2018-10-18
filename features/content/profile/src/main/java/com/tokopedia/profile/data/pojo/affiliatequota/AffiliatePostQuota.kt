package com.tokopedia.profile.data.pojo.affiliatequota

import com.google.gson.annotations.SerializedName

data class AffiliatePostQuota(
        @SerializedName("number")
        var number: Int = 0,

        @SerializedName("formatted")
        val formatted: String = "",

        @SerializedName("format")
        val format: String = ""
)