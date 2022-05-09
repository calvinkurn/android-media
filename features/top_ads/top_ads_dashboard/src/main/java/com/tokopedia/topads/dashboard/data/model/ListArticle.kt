package com.tokopedia.topads.dashboard.data.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

class ListArticle : ArrayList<ListArticle.ListArticleItem>() {

    @Parcelize
    data class ListArticleItem(
        @SerializedName("articles")
        val articles: List<Article>,
        @SerializedName("categoryName")
        val categoryName: String?,
        @SerializedName("description")
        val description: String?,
    ) : Parcelable {

        @Parcelize
        data class Article(
            @SerializedName("description")
            val description: String?,
            @SerializedName("slug")
            val slug: String?,
            @SerializedName("thumbnail")
            val thumbnail: String?,
            @SerializedName("title")
            val title: String?,
        ) : Parcelable
    }
}