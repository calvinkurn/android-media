package com.tokopedia.instantloan.data.model.response

import com.google.gson.annotations.SerializedName

data class GqlLendingCategory(
        @SerializedName("data")
        val categoryData: ArrayList<GqlLendingCategoryData>
)