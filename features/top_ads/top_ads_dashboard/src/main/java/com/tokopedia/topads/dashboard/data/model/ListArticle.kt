package com.tokopedia.topads.dashboard.data.model


import com.google.gson.annotations.SerializedName

class ListArticle : ArrayList<ListArticle.ListArticleItem>() {
    data class ListArticleItem(
        @SerializedName("articles")
        val articles: List<Article>,
        @SerializedName("categoryName")
        val categoryName: String?,
        @SerializedName("description")
        val description: String?
    ) {
        data class Article(
            @SerializedName("description")
            val description: String?,
            @SerializedName("slug")
            val slug: String?,
            @SerializedName("thumbnail")
            val thumbnail: String?,
            @SerializedName("title")
            val title: String?
        )
    }
}