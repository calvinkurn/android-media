package com.tokopedia.affiliate.model.response

import com.google.gson.annotations.SerializedName

data class AffiliateEducationSearchArticleCardsResponse(

    @SerializedName("searchEducation")
    val searchEducation: SearchEducation? = null
) {
    data class SearchEducation(

        @SerializedName("data")
        val data: Data? = null
    ) {
        data class Data(

            @SerializedName("results")
            val results: List<ResultsItem?>? = null,

            @SerializedName("status")
            val status: String? = null
        ) {
            data class ResultsItem(

                @SerializedName("section")
                val section: List<SectionItem?>? = null
            ) {
                data class SectionItem(

                    @SerializedName("meta")
                    val meta: Meta? = null,

                    @SerializedName("id")
                    val id: String? = null,

                    @SerializedName("sort")
                    val sort: List<SortItem?>? = null,

                    @SerializedName("title")
                    val title: String? = null,

                    @SerializedName("items")
                    val items: List<Items?>? = null
                ) {
                    data class Meta(

                        @SerializedName("offset")
                        val offset: Int? = null,

                        @SerializedName("totalHits")
                        val totalHits: Int? = null,

                        @SerializedName("limit")
                        val limit: Int? = null
                    )

                    data class SortItem(

                        @SerializedName("name")
                        val name: String? = null,

                        @SerializedName("id")
                        val id: String? = null,

                        @SerializedName("value")
                        val value: String? = null
                    )

                    data class Items(

                        @SerializedName("publishTime")
                        val publishTime: String? = null,

                        @SerializedName("thumbnail")
                        val thumbnail: Thumbnail? = null,

                        @SerializedName("modifiedDate")
                        val modifiedDate: String? = null,

                        @SerializedName("description")
                        val description: String? = null,

                        @SerializedName("appURL")
                        val appURL: String? = null,

                        @SerializedName("attributes")
                        val attributes: Attributes? = null,

                        @SerializedName("categories")
                        val categories: List<CategoriesItem?>? = null,

                        @SerializedName("title")
                        val title: String? = null,

                        @SerializedName("url")
                        val url: String? = null
                    ) {
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

                        data class CategoriesItem(

                            @SerializedName("level")
                            val level: Int? = null,

                            @SerializedName("id")
                            val id: Long? = null,

                            @SerializedName("title")
                            val title: String? = null
                        )

                        data class Attributes(

                            @SerializedName("read_time")
                            val readTime: String? = null
                        )
                    }
                }
            }
        }
    }
}
