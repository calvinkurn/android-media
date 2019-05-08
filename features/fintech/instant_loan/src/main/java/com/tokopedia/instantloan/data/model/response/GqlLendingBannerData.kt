package com.tokopedia.instantloan.data.model.response

import com.google.gson.annotations.SerializedName

data class GqlLendingBannerData(
        @SerializedName("ID")
        var bannerId: Int,

        @SerializedName("Link")
        var bannerLink: String,

        @SerializedName("UrlImage")
        var bannerImageUrl: String

)