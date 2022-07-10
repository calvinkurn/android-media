package com.tokopedia.shopadmin.feature.invitationaccepted.domain.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ArticleDetailResponse(
    @SerializedName("articleDetail")
    @Expose
    val articleDetail: ArticleDetail = ArticleDetail()
) {
    data class ArticleDetail(
        @SerializedName("data")
        @Expose
        val `data`: Data = Data()
    ) {
        data class Data(
            @SerializedName("blog")
            @Expose
            val blog: Blog = Blog()
        ) {
            data class Blog(
                @SerializedName("html_content")
                @Expose
                val htmlContent: String = "",
                @SerializedName("title")
                @Expose
                val title: String = ""
            )
        }
    }
}