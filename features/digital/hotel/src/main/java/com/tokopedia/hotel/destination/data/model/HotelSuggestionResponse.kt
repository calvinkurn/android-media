package com.tokopedia.hotel.destination.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by jessica on 01/04/19
 */

data class HotelSuggestionResponse(
        @SerializedName("data")
        @Expose
        var searchDestinationList: List<SearchDestination>,

        @SerializedName("meta")
        @Expose
        var hotelSuggestionMeta: HotelSuggestionResponse.Meta
) {
    data class Meta(
            @SerializedName("keyword")
            @Expose
            var keyword: String,

            @SerializedName("total_suggestion")
            @Expose
            var totalSuggestion: Int
    )
}