package com.tokopedia.topads.dashboard.data.model.beranda


import com.google.gson.annotations.SerializedName

class TopAdsLatestReading : ArrayList<TopAdsLatestReading.TopAdsLatestReadingItem>(){
    data class TopAdsLatestReadingItem(
        @SerializedName("articles")
        val articles: List<Article>,
        @SerializedName("categoryName")
        val categoryName: String,
        @SerializedName("description")
        val description: String,
        @SerializedName("unifyIcon")
        val unifyIcon: String
    ) {
        data class Article(
            @SerializedName("description")
            val description: String,
            @SerializedName("slug")
            val slug: String,
            @SerializedName("thumbnail")
            val thumbnail: String,
            @SerializedName("title")
            val title: String
        )
    }
}