package com.tokopedia.salam.umrah.homepage.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UmrahHomepageBannerParam(
        @SerializedName("service")
        @Expose
        val service : String = "UMRAHCATALOG",
        @SerializedName("location")
        @Expose
        val location : String = "homepage"
)