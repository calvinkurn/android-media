package com.tokopedia.hotel.destination.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by jessica on 01/04/19
 */

data class HotelSuggestion(
    @SerializedName("data")
    @Expose
    var searchDestinationList: List<SearchDestination> = listOf(),

    @SerializedName("meta")
    @Expose
    var hotelSuggestionMeta: HotelSuggestion.Meta = Meta(),

    @SerializedName("source")
    val source: String = ""
) {
    data class Response(
        @SerializedName("propertySearchSuggestion")
        @Expose
        var propertySearchSuggestion: HotelSuggestion = HotelSuggestion()
    )

    data class Meta(
        @SerializedName("keyword")
        @Expose
        var keyword: String = "",

        @SerializedName("total_suggestion")
        @Expose
        var totalSuggestion: Int = 0
    )
}
