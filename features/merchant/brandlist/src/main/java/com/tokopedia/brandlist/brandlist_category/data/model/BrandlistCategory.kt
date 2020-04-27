package com.tokopedia.brandlist.brandlist_category.data.model

import com.google.gson.annotations.SerializedName

data class BrandlistCategories(
        @SerializedName("categories")
        val categories: MutableList<Category> = mutableListOf()
) {
    data class Response(
            @SerializedName("OfficialStoreCategories")
            val OfficialStoreCategories : BrandlistCategories = BrandlistCategories()
    )
}