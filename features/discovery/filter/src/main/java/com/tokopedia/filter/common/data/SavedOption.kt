package com.tokopedia.filter.common.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

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

    companion object {
        const val SORT_SAVED_OPTION_TITLE = "sort"

        fun create(option: Option, filterList: List<Filter>) =
            SavedOption(
                key = option.key,
                value = option.value,
                name = option.name,
                title = filterList.find { it.options.contains(option) }?.title ?: "",
            )

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