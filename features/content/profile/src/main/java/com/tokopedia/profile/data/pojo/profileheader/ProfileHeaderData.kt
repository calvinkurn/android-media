package com.tokopedia.profile.data.pojo.profileheader

import com.google.gson.annotations.SerializedName

data class ProfileHeaderData(
        @SerializedName("bymeProfileHeader")
        val bymeProfileHeader: BymeProfileHeader = BymeProfileHeader()
)