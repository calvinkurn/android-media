package com.tokopedia.instantloan.data.model.response

import com.google.gson.annotations.SerializedName

data class GqlLendingCategoryData (
        @SerializedName("ID")
        var categoryId: Int,

        @SerializedName("Name")
        var categoryName: String,

        @SerializedName("NameSlug")
        var categoryNameSlug: String,

        @SerializedName("Icon")
        var categoryIconUrl: String
)