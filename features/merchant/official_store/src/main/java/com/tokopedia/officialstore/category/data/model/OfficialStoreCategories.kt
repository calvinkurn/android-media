package com.tokopedia.officialstore.category.data.model

import com.google.gson.annotations.SerializedName

data class OfficialStoreCategories(
    @SerializedName("categories")
    val categories: MutableList<Category> = mutableListOf()
) {
    data class Response(
            @SerializedName("OfficialStoreCategories")
            val OfficialStoreCategories : OfficialStoreCategories = OfficialStoreCategories()
    )
}