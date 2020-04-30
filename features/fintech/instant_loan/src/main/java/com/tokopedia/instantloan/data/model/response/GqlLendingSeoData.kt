package com.tokopedia.instantloan.data.model.response

import com.google.gson.annotations.SerializedName

data class GqlLendingSeoData(
        @SerializedName("Head")
        var seoHead: String,

        @SerializedName("Body")
        var seoBody: String
)
