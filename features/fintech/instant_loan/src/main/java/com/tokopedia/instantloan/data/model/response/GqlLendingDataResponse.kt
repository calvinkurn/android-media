package com.tokopedia.instantloan.data.model.response

import com.google.gson.annotations.SerializedName

data class GqlLendingDataResponse (
        @SerializedName("category")
        var leCategory: GqlLendingCategory,

        @SerializedName("banner")
        var leBanner: GqlLendingBanner,

        @SerializedName("partner")
        var lePartner: GqlLendingPartner,

        @SerializedName("seo")
        var leSeo: GqlLendingSeo? = GqlLendingSeo(ArrayList<GqlLendingSeoData>())
)