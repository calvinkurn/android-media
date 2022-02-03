package com.tokopedia.topads.dashboard.data.model.beranda


import com.google.gson.annotations.SerializedName

data class TopAdsLatestReading(
    @SerializedName("categoryTree")
    val categoryTree: CategoryTree
) {
    data class CategoryTree(
        @SerializedName("data")
        val `data`: Data
    ) {
        data class Data(
            @SerializedName("categories")
            val categories: List<Category>,
            @SerializedName("status")
            val status: String
        ) {
            data class Category(
                @SerializedName("children")
                val children: List<Children>,
                @SerializedName("description")
                val description: String,
                @SerializedName("icon")
                val icon: Icon,
                @SerializedName("id")
                val id: String,
                @SerializedName("title")
                val title: String,
                @SerializedName("url")
                val url: String
            ) {
                data class Children(
                    @SerializedName("description")
                    val description: String,
                    @SerializedName("id")
                    val id: String,
                    @SerializedName("title")
                    val title: String,
                    @SerializedName("url")
                    val url: String
                )

                data class Icon(
                    @SerializedName("url")
                    val url: String
                )
            }
        }
    }
}