package com.tokopedia.profile.data.pojo.profileheader

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class BymeProfileHeader(
        @SerializedName("profile")
        @Expose
        val profile: Profile = Profile(),

        @SerializedName("error")
        @Expose
        val profileHeaderError: ProfileHeaderError = ProfileHeaderError()
)