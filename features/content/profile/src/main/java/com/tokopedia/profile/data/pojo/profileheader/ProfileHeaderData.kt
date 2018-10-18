package com.tokopedia.profile.data.pojo.profileheader

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProfileHeaderData(
        @SerializedName("bymeProfileHeader")
        @Expose
        val bymeProfileHeader: BymeProfileHeader = BymeProfileHeader()
)