package com.tokopedia.salam.umrah.homepage.data

import com.google.gson.annotations.SerializedName

data class UmrahHomepageCategoryParam(
        @SerializedName("limit")
        var limit: Int  = 5)