package com.tokopedia.travel_slice.hotel.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by jessica on 16/10/20
 */

data class SuggestionCity(
        @SerializedName("searchType")
        @Expose
        val searchType: String = "",

        @SerializedName("searchID")
        @Expose
        val searchId: String =  ""
) {
    data class Response(
            @SerializedName("propertySearchSuggestion")
            @Expose
            val response: SuggestionData = SuggestionData()
    )

    data class SuggestionData(
            @SerializedName("data")
            @Expose
            val data: List<SuggestionCity> = listOf()
    )
}