package com.tokopedia.instantloan.data.model.response

import com.google.gson.annotations.SerializedName

data class GqlLendingSeo(
        @SerializedName("data")
        var seoData: ArrayList<GqlLendingSeoData>? = ArrayList()
)