package com.tokopedia.salam.umrah.homepage.data

import com.google.gson.annotations.SerializedName


data class UmrahHomepageCategoryFeaturedParam(
        @SerializedName("limit")
        var limit: Int  = 2,
        @SerializedName("flags")
        var flags: String  = "CATEGORY_FEATURED_ON_HOMEPAGE",
        @SerializedName("page")
        var page: Int  = 1
)
