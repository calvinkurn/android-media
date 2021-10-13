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

    fun asOption(): Option = Option(key = key, value = value, name = name)

    companion object {
        fun create(option: Option, filterList: List<Filter>) =
            SavedOption(
                key = option.key,
                value = option.value,
                name = option.name,
                title = filterList.find { it.options.contains(option) }?.title ?: "",
            )
    }
}