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
    ){
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is RecommendationFilterChip) return false

            if (name != other.name) return false
            if (value != other.value) return false
            if (inputType != other.inputType) return false
            if (isActivated != other.isActivated) return false
            if (options != other.options) return false

            return true
        }

        override fun hashCode(): Int {
            var result = name.hashCode()
            result = HASH_CODE * result + value.hashCode()
            result = HASH_CODE * result + inputType.hashCode()
            result = HASH_CODE * result + isActivated.hashCode()
            result = HASH_CODE * result + options.hashCode()
            return result
        }
    }

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

    companion object{
        private const val HASH_CODE = 31
    }
}