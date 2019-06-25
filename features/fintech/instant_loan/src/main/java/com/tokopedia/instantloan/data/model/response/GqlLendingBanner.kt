package com.tokopedia.instantloan.data.model.response

import com.google.gson.annotations.SerializedName

data class GqlLendingBanner(
        @SerializedName("data")
        var bannerData: ArrayList<GqlLendingBannerData>
)