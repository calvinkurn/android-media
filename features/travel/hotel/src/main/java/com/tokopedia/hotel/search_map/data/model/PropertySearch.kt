package com.tokopedia.hotel.search_map.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PropertySearch(
        @SerializedName("propertyList")
        @Expose
        val properties: List<Property> = listOf(),

        @SerializedName("propertyDisplayInfo")
        @Expose
        val displayInfo: DisplayInfo = DisplayInfo(),

        @SerializedName("filters")
        @Expose
        val filters: List<FilterV2> = listOf(),

        @SerializedName("quickFilter")
        @Expose
        val quickFilter: List<QuickFilter> = listOf()
) {

    data class Response(
            @SerializedName("propertySearch")
            @Expose
            val response: PropertySearch = PropertySearch()
    )
}