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
                    val thumbnail: Thumbnail? = null,

                    @SerializedName("publish_time")
                    val publishTime: String? = null,

                    @SerializedName("description")
                    val description: String? = null,

                    @SerializedName("attributes")
                    val attributes: Attributes? = null,

                    @SerializedName("categories")
                    val categories: List<CategoriesItem?>? = null,

                    @SerializedName("title")
                    val title: String? = null,

                    @SerializedName("modified_date")
                    val modifiedDate: String? = null,

                    @SerializedName("youtube_url")
                    val youtubeUrl: String? = null,

                    @SerializedName("upload_datetime")
                    val uploadDatetime: String? = null,

                    @SerializedName("slug")
                    val slug: String? = null,

                    @SerializedName("id")
                    val articleId: Long? = null

                ) {

                    data class Attributes(

                        @SerializedName("read_time")
                        val readTime: String? = null
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
