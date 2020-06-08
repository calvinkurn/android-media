package com.tokopedia.instantloan.data.model.response

import com.google.gson.annotations.SerializedName

data class GqlLendingBannerData(
        @SerializedName("Link")
        var bannerLink: String,

        @SerializedName("UrlImage")
        var bannerImageUrl: String
)
