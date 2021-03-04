package com.tokopedia.recommendation_widget_common.data

import com.google.gson.annotations.SerializedName

/**
 * Created by Lukas on 04/08/20.
 */

data class RecommendationFilterChipsEntity (
        @SerializedName("recommendationFilterChips")
        val recommendationFilterChips: RecommendationFilterChips = RecommendationFilterChips()
){
    data class RecommendationFilterChips(@SerializedName("data") val data: FilterAndSort = FilterAndSort())

    data class FilterAndSort(
        @SerializedName("filter") val filterChip: List<RecommendationFilterChip> = listOf(),
        @SerializedName("sort") val sortChip: List<RecommendationSortChip> = listOf()
    )

    data class RecommendationSortChip(
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
            @SerializedName("key")
            val key: String = "",
            var isSelected: Boolean = false
    )

    data class  RecommendationFilterChip(
            @SerializedName("title")
            val title: String = "",
            @SerializedName("name")
            val name: String = "",
            @SerializedName("value")
            val value: String = "",
            @SerializedName("inputType")
            val inputType: String = "",
            @SerializedName("templateName")
            val templateName: String = "",
            @SerializedName("icon")
            val icon: String = "",
            @SerializedName("isActivated")
            var isActivated: Boolean = false,
            @SerializedName("search")
            var search: Search = Search(),
            @SerializedName("options")
            val options: List<Option> = listOf()
    ){
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is RecommendationFilterChip) return false

            if (title != other.title) return false
            if (value != other.value) return false
            if (inputType != other.inputType) return false
            if (templateName != other.templateName) return false
            if (icon != other.icon) return false
            if (isActivated != other.isActivated) return false
            if (options != other.options) return false

            return true
        }

        override fun hashCode(): Int {
            var result = title.hashCode()
            result = HASH_CODE * result + value.hashCode()
            result = HASH_CODE * result + inputType.hashCode()
            result = HASH_CODE * result + templateName.hashCode()
            result = HASH_CODE * result + icon.hashCode()
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
            @SerializedName("isPopular")
            val isPopular: Boolean = false,
            @SerializedName("isActivated")
            var isActivated: Boolean = false,
            @SerializedName(value = "hex_color", alternate = ["hexColor"])
            var hexColor: String = "",
            @SerializedName(value = "val_min", alternate = ["valMin"])
            var valMin: String = "",
            @SerializedName(value = "val_max", alternate = ["valMax"])
            var valMax: String = "",
            @SerializedName(value = "description", alternate = ["Description"])
            var description: String = "",
            @SerializedName(value = "is_new", alternate = ["isNew"])
            var isNew: Boolean = false,
            @SerializedName("child")
            val children: List<Child> = listOf()
    )

    data class Child(
            @SerializedName("name")
            val name: String = "",
            @SerializedName("value")
            val value: String = "",
            @SerializedName("inputType")
            val inputType: String = "",
            @SerializedName("icon")
            val icon: String = "",
            @SerializedName("key")
            val key: String = ""
    )

    data class Search(@SerializedName("searchable")
                 var searchable: Int = 0,
                 @SerializedName("placeholder")
                 var placeholder: String = "")

    companion object{
        private const val HASH_CODE = 31
    }
}