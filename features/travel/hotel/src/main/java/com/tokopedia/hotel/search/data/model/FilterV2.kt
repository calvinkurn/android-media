package com.tokopedia.hotel.search.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by jessica on 12/08/20
 */

data class FilterV2(
        @SerializedName("type")
        @Expose
        val type: String = "",

        @SerializedName("name")
        @Expose
        val name: String = "",

        @SerializedName("displayName")
        @Expose
        val displayName: String = "",

        @SerializedName("options")
        @Expose
        var options: List<String> = listOf(),

        var optionSelected: List<String> = listOf(),

        var defaultOption: String = "",

        @SerializedName("image")
        @Expose
        val image: Images = Images()
) {
    data class Images(
            @SerializedName("light")
            @Expose
            val light: String = "",

            @SerializedName("dark")
            @Expose
            val dark: String = ""
    )

    companion object {
            const val FILTER_TYPE_SELECTION = "selection"
            const val FILTER_TYPE_OPEN_RANGE = "open_range"
            const val FILTER_TYPE_SELECTION_RANGE = "selection_range"
            const val FILTER_TYPE_SORT = "sort"
    }
}