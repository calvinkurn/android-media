package com.tokopedia.content.common.producttag.model

import com.google.gson.annotations.SerializedName

/**
 * Created By : Jonathan Darwin on May 13, 2022
 */
data class FeedQuickFilterResponse(
    @SerializedName("quick_filter")
    val wrapper: Wrapper = Wrapper(),
) {
    data class Wrapper(
        @SerializedName("filter")
        val filter: List<Filter> = emptyList(),
    )

    data class Filter(
        @SerializedName("title")
        val title: String = "",

        @SerializedName("options")
        val options: List<FilterOption> = emptyList(),
    )

    data class FilterOption(
        @SerializedName("name")
        val name: String = "",

        @SerializedName("key")
        val key: String = "",

        @SerializedName("icon")
        val icon: String = "",

        @SerializedName("value")
        val value: String = "",
    )
}