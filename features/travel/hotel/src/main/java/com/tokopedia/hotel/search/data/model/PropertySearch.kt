package com.tokopedia.hotel.search.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PropertySearch(
        @SerializedName("propertyList")
        @Expose
        val properties: List<Property> = listOf(),

        @SerializedName("propertyDisplayInfo")
        @Expose
        val displayInfo: DisplayInfo = DisplayInfo()
) {

    data class Response(
            @SerializedName("propertySearch")
            @Expose
            val response: PropertySearch = PropertySearch()
    )
}