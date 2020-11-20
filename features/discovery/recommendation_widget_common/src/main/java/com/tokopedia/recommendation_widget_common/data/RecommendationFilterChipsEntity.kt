package com.tokopedia.recommendation_widget_common.data

import com.google.gson.annotations.SerializedName

/**
 * Created by Lukas on 04/08/20.
 */

data class RecommendationFilterChipsEntity (
        @SerializedName("recommendationFilterChips")
        val recommendationFilterChips: RecommendationFilterChips = RecommendationFilterChips()
){
    data class RecommendationFilterChips(@SerializedName("data") val data: Filter = Filter())

    data class Filter(@SerializedName("filter") val filterChip: List<RecommendationFilterChip> = listOf())

    data class RecommendationFilterChip(
            @SerializedName("name")
            val name: String = "",
            @SerializedName("value")
            val value: String = "",
            @SerializedName("title")
            val title: String = "",
            @SerializedName("templateName")
            val templateName: String = "",
            @SerializedName("inputType")
            val inputType: String = "",
            @SerializedName("isActivated")
            val isActivated: Boolean = false,
            @SerializedName("options")
            val options: List<Option> = listOf()
    )

    data class Option(
            @SerializedName("name")
            val name: String = "",
            @SerializedName("value")
            val value: String = "",
            @SerializedName("key")
            val key: String = "",
            @SerializedName("icon")
            val icon: String = "",
            @SerializedName("inputType")
            val inputType: String = "",
            @SerializedName("isActivated")
            val isActivated: Boolean = false
    )
}