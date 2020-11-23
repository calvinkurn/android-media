package com.tokopedia.homenav.mainnav.data.pojo.user

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.homenav.mainnav.data.pojo.membership.TokopointsPojo

data class UserPojo(
        @SerializedName("profile")
        @Expose
        val profile: ProfilePojo = ProfilePojo()
)

data class ProfilePojo(

        @SerializedName("full_name")
        @Expose
        val name: String = "",
        @SerializedName("profilePicture")
        @Expose
        val profilePicture: String = ""
)