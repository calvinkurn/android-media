package com.tokopedia.tokomart.category.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CategoryModel(
        @SerializedName("total_data")
        @Expose
        val totalData: Int = 0,
) {
}