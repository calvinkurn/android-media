package com.tokopedia.home_explore_category.domain.model


import com.google.gson.annotations.SerializedName

data class GetExploreCategoryResponse(
    @SerializedName("dynamicHomeIcon")
    val dynamicHomeIcon: DynamicHomeIcon = DynamicHomeIcon()
) {
    data class DynamicHomeIcon(
        @SerializedName("categoryGroup")
        val categoryGroup: List<CategoryGroup> = listOf()
    ) {
        data class CategoryGroup(
            @SerializedName("categoryRows")
            val categoryRows: List<CategoryRow> = listOf(),
            @SerializedName("desc")
            val desc: String = "",
            @SerializedName("id")
            val id: String = "0",
            @SerializedName("imageUrl")
            val imageUrl: String = "",
            @SerializedName("title")
            val title: String = ""
        ) {
            data class CategoryRow(
                @SerializedName("applinks")
                val applinks: String = "",
                @SerializedName("bu_identifier")
                val buIdentifier: String = "",
                @SerializedName("categoryLabel")
                val categoryLabel: String = "",
                @SerializedName("id")
                val id: String = "0",
                @SerializedName("imageUrl")
                val imageUrl: String = "",
                @SerializedName("name")
                val name: String = "",
                @SerializedName("url")
                val url: String = ""
            )
        }
    }
}
