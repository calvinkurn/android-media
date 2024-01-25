package com.tokopedia.home_explore_category.domain.model

import com.google.gson.annotations.SerializedName

data class GetHomeExploreCategoryResponse(
    @SerializedName("getHomeCategoryV2") val getHomeCategoryV2: GetHomeCategoryV2 = GetHomeCategoryV2()
) {
    data class GetHomeCategoryV2(
        @SerializedName("categories") val categories: List<Category> = listOf()
    ) {
        data class Category(
            @SerializedName("categoryRows") val categoryRows: List<CategoryRow> = listOf(),
            @SerializedName("desc") val desc: String = "",
            @SerializedName("id") val id: String = "0",
            @SerializedName("imageUrl") val imageUrl: String = "",
            @SerializedName("title") val title: String = ""
        ) {
            data class CategoryRow(
                @SerializedName("applinks") val applinks: String = "",
                @SerializedName("buIdentifier") val buIdentifier: String = "",
                @SerializedName("categoryLabel") val categoryLabel: String = "",
                @SerializedName("id") val id: String = "0",
                @SerializedName("imageUrl") val imageUrl: String = "",
                @SerializedName("name") val name: String = "",
                @SerializedName("url") val url: String = ""
            )
        }
    }
}
