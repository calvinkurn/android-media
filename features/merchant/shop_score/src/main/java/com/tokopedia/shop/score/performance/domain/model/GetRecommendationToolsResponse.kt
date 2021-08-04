package com.tokopedia.shop.score.performance.domain.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetRecommendationToolsResponse(
        @Expose
        @SerializedName("valuePropositionGetRecommendationTools")
        val valuePropositionGetRecommendationTools: ValuePropositionGetRecommendationTools = ValuePropositionGetRecommendationTools()
) {
    data class ValuePropositionGetRecommendationTools(
            @Expose
            @SerializedName("recommendation_tools")
            val recommendationTools: List<RecommendationTool> = listOf()
    ) {
        data class RecommendationTool(
                @Expose
                @SerializedName("image_url")
                val imageUrl: String = "",
                @Expose
                @SerializedName("text")
                val text: String = "",
                @Expose
                @SerializedName("title")
                val title: String = "",
                @Expose
                @SerializedName("related_link_applink")
                val relatedLinkAppLink: String = "",
                @Expose
                @SerializedName("identifier")
                val identifier: String = ""
        )
    }
}