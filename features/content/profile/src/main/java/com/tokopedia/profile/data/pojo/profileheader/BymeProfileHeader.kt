package com.tokopedia.profile.data.pojo.profileheader

import com.google.gson.annotations.SerializedName

data class BymeProfileHeader(
        @SerializedName("profile")
        val profile: Profile = Profile()
)