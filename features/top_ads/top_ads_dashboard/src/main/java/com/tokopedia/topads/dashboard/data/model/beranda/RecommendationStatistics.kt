package com.tokopedia.topads.dashboard.data.model.beranda

import com.google.gson.annotations.SerializedName

sealed class KataKunciHomePageBase

data class KataKunciSimpleButton(val label: String) : KataKunciHomePageBase()

data class ImageModel(
    @SerializedName("ImageURL")
    val imageUrl: String
) {
    var overLappingText: String? = ""
}

data class RecommendationStatistics(
    @SerializedName("topadsWidgetRecommendationStatistics")
    val statistics: Statistics
) {
    data class Statistics(
        @SerializedName("data")
        val data: Data,
        @SerializedName("errors")
        val errors: List<Any>,
        @SerializedName("header")
        val header: Header
    ) {
        data class Data(
            @SerializedName("DailyBudgetRecommandationStats")
            val dailyBudgetRecommendationStats: DailyBudgetRecommendationStats,
            @SerializedName("KeywordRecommendationStats")
            val keywordRecommendationStats: KeywordRecommendationStats,
            @SerializedName("ProductRecommmandationStats")
            val productRecommendationStats: ProductRecommendationStats
        ) {
            data class DailyBudgetRecommendationStats(
                @SerializedName("Count")
                val count: Int,
                @SerializedName("GroupList")
                val groupList: List<GroupInfo>,
                @SerializedName("TotalClicks")
                val totalClicks: Int
            ) {
                data class GroupInfo(
                    @SerializedName("GroupName")
                    val groupName: String
                )
            }

            data class KeywordRecommendationStats(
                @SerializedName("GroupCount")
                val groupCount: Int,
                @SerializedName("InsightCount")
                val insightCount: Int,
                @SerializedName("TopGroups")
                val topGroups: List<TopGroup>
            ) {
                data class TopGroup(
                    @SerializedName("BidCount")
                    val bidCount: Int = 0,
                    @SerializedName("BidTotalImpression")
                    val bidTotalImpression: Int = 0,
                    @SerializedName("GroupName")
                    val groupName: String = "",
                    @SerializedName("NegativeKeywordCount")
                    val negativeKeywordCount: Int = 0,
                    @SerializedName("NegativeKeywordPotentialSave")
                    val negativeKeywordPotentialSave: Int = 0,
                    @SerializedName("NewKeywordCount")
                    val newKeywordCount: Int = 0,
                    @SerializedName("NewKeywordTotalImpression")
                    val newKeywordTotalImpression: Int = 0
                ) : KataKunciHomePageBase() {
                    var isSelected = false
                }
            }

            data class ProductRecommendationStats(
                @SerializedName("Count")
                val count: Int,
                @SerializedName("ProductList")
                val productList: List<ImageModel>,
                @SerializedName("TotalSearchCount")
                val totalSearchCount: Int
            )
        }

        data class Header(
            @SerializedName("process_time")
            val processTime: Double
        )
    }
}