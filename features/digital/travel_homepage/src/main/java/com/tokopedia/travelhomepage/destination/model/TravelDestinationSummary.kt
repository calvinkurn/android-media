package com.tokopedia.travelhomepage.destination.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by jessica on 2019-12-23
 */

data class TravelDestinationSummary (
        @SerializedName("title")
        @Expose
        val title: String = "",

        @SerializedName("images")
        @Expose
        val images: List<Images> = listOf(),

        @SerializedName("description")
        @Expose
        val description: String = ""
) {
    data class Response(
            @SerializedName("TravelDestinatinoSummary")
            @Expose
            val response: TravelDestinationSummary = TravelDestinationSummary()
    )

    data class Images(
            @SerializedName("id")
            @Expose
            val id: String = "",

            @SerializedName("imageURL")
            @Expose
            val imageUrl: String = ""
    )
}