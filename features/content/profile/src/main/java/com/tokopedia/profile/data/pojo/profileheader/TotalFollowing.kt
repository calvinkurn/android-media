package com.tokopedia.profile.data.pojo.profileheader

import com.google.gson.annotations.SerializedName

data class TotalFollowing(
        @SerializedName("number")
        val number: Int = 0,

        @SerializedName("formatted")
        val formatted: String = ""
)