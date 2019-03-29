package com.tokopedia.profile.data.pojo.profileheader

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TotalFollower(
        @SerializedName("number")
        @Expose
        val number: Int = 0,

        @SerializedName("formatted")
        @Expose
        val formatted: String = ""
)