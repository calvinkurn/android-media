package com.tokopedia.filter.common.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.discovery.common.constants.SearchApiConst

data class SavedOption(
    @SerializedName("title")
    @Expose
    val title: String = "",

    @SerializedName("key")
    @Expose
    val key: String = "",

    @SerializedName("value")
    @Expose
    val value: String = "",

    @SerializedName("name")
    @Expose
    val name: String = "",
) {

    fun asOption(): Option = Option(
        key = key,
        value = value,
        name = name,
        inputState = true.toString(),
    )

    fun isSort(): Boolean = key == SearchApiConst.OB

    fun isFilter(): Boolean = !isSort()

    companion object {
        const val SORT_SAVED_OPTION_TITLE = "sort"

        fun create(option: Option, filterList: List<Filter>): SavedOption =
            SavedOption(
                key = option.key,
                value = option.value,
                name = option.name,
                title = findFilterWithOption(filterList, option)?.title ?: "",
            )

        private fun findFilterWithOption(filterList: List<Filter>, optionToFind: Option) =
            filterList.find { it.allOptions().contains(optionToFind) }

        private fun Filter.allOptions(): List<Option> {
            val levelTwoCategoryList = options
                .flatMap(Option::levelTwoCategoryList)

            val levelThreeCategoryList = levelTwoCategoryList
                .flatMap(LevelTwoCategory::levelThreeCategoryList)

            return options +
                levelTwoCategoryList.map(LevelTwoCategory::asOption) +
                levelThreeCategoryList.map(LevelThreeCategory::asOption)
        }

        fun create(sort: Sort): SavedOption {
            return SavedOption(
                key = sort.key,
                value = sort.value,
                name = sort.name,
                title = SORT_SAVED_OPTION_TITLE,
            )
        }
    }
}