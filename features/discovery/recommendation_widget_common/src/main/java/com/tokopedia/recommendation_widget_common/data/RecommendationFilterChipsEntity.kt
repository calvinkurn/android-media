package com.tokopedia.recommendation_widget_common.data

import com.google.gson.annotations.SerializedName

/**
 * Created by Lukas on 04/08/20.
 */

data class RecommendationFilterChipsEntity (
        @SerializedName("data")
        val data: List<RecommendationFilterChip> = listOf()
){

    data class RecommendationFilterChip(
            @SerializedName("name")
            val name: String = "",
            @SerializedName("value")
            val value: String = "",
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
            @SerializedName("inputType")
            val inputType: String = "",
            @SerializedName("isActivated")
            val isActivated: Boolean = false
    )
}