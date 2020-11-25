package com.tokopedia.feedcomponent.data.pojo.profileheader

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class BymeProfileHeader(
        @SerializedName("profile")
        @Expose
        val profile: Profile = Profile(),

        @SerializedName("error")
        @Expose
        val profileHeaderError: ProfileHeaderError = ProfileHeaderError(),

        @SerializedName("config")
        @Expose
        val profileConfig: ProfileConfig = ProfileConfig()
)