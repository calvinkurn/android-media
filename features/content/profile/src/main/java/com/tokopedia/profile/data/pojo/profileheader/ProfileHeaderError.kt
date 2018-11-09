package com.tokopedia.profile.data.pojo.profileheader

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProfileHeaderError(
        @SerializedName("code")
        @Expose
        val code: Int = 0,

        @SerializedName("message")
        @Expose
        val message: String = "",

        @SerializedName("type")
        @Expose
        val type: String = ""
)