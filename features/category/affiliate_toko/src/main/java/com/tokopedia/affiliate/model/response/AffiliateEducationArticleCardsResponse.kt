package com.tokopedia.affiliate.model.response

import com.google.gson.annotations.SerializedName

data class AffiliateEducationArticleCardsResponse(

    @SerializedName("cardsArticle")
    val cardsArticle: CardsArticle? = null
) {
    data class CardsArticle(

        @SerializedName("data")
        val data: Data? = null
    ) {
        data class Data(

            @SerializedName("cards")
            val cards: List<CardsItem?>? = null,

            @SerializedName("status")
            val status: String? = null
        ) {
            data class CardsItem(

                @SerializedName("action_link")
                val actionLink: String? = null,

                @SerializedName("offset")
                val offset: Int? = null,

                @SerializedName("action_title")
                val actionTitle: String? = null,

                @SerializedName("total_count")
                val totalCount: Int? = null,

                @SerializedName("app_action_link")
                val appActionLink: String? = null,

                @SerializedName("limit")
                val limit: Int? = null,

                @SerializedName("id")
                val id: String? = null,

                @SerializedName("has_more")
                val hasMore: Boolean? = null,

                @SerializedName("title")
                val title: String? = null,

                @SerializedName("items")
                val articles: List<Article?>? = null
            ) {
                data class Article(

                    @SerializedName("duration")
                    val duration: String? = null,

                    @SerializedName("thumbnail")
                    var thumbnail: Thumbnail? = null,

                    @SerializedName("publish_time")
                    var publishTime: String? = null,

                    @SerializedName("description")
                    var description: String? = null,

                    @SerializedName("attributes")
                    var attributes: Attributes? = null,

                    @SerializedName("categories")
                    var categories: List<CategoriesItem?>? = null,

                    @SerializedName("title")
                    var title: String? = null,

                    @SerializedName("modified_date")
                    var modifiedDate: String? = null,

                    @SerializedName("youtube_url")
                    var youtubeUrl: String? = null,

                    @SerializedName("upload_datetime")
                    val uploadDatetime: String? = null,

                    @SerializedName("slug")
                    var slug: String? = null,

                    @SerializedName("id")
                    var articleId: Long? = null

                ) {

                    data class Attributes(

                        @SerializedName("read_time")
                        var readTime: String? = null
                    )

                    data class CategoriesItem(

                        @SerializedName("level")
                        val level: Int? = null,

                        @SerializedName("id")
                        val id: Long? = null,

                        @SerializedName("title")
                        val title: String? = null
                    )

                    data class Thumbnail(

                        @SerializedName("desktop")
                        val desktop: String? = null,

                        @SerializedName("android")
                        val android: String? = null,

                        @SerializedName("mobile")
                        val mobile: String? = null,

                        @SerializedName("ios")
                        val ios: String? = null
                    )
                }
            }
        }
    }
}
