package com.tokopedia.homenav.mainnav.data.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MainNavPojo(
        @SerializedName("id")
        @Expose
        val id: String = ""
)