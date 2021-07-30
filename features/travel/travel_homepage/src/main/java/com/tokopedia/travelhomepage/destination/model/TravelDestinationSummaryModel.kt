package com.tokopedia.travelhomepage.destination.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.travelhomepage.destination.factory.TravelDestinationAdapterTypeFactory

/**
 * @author by jessica on 2019-12-23
 */

data class TravelDestinationSummaryModel (
        @SerializedName("title")
        @Expose
        val title: String = "",

        @SerializedName("images")
        @Expose
        val images: List<Images> = listOf(),

        @SerializedName("description")
        @Expose
        val description: String = ""
): TravelDestinationItemModel() {
    override fun type(typeFactory: TravelDestinationAdapterTypeFactory): Int = typeFactory.type(this)

    data class Response(
            @SerializedName("TravelDestinationSummary")
            @Expose
            val response: TravelDestinationSummaryModel = TravelDestinationSummaryModel()
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