package com.tokopedia.profile.data.pojo.profileheader

import com.google.gson.annotations.SerializedName

data class AffiliatePostQuota(
        @SerializedName("number")
        val number: Int = 0,

        @SerializedName("formatted")
        val formatted: String = ""
)