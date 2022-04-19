package com.tokopedia.minicart.common.data.response.minicartlist

import com.google.gson.annotations.SerializedName

data class Category(
        @SerializedName("category_id")
        val categoryId: String = "",
        @SerializedName("category_name")
        val categoryName: String = ""
)