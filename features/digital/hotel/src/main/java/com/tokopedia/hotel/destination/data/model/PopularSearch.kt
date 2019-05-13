package com.tokopedia.hotel.destination.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.hotel.destination.view.adapter.PopularSearchTypeFactory

/**
 * @author by jessica on 25/03/19
 */

data class PopularSearch(

        @SerializedName("destinationID")
        @Expose
        val destinationId: Int = 0,

        @SerializedName("type")
        @Expose
        val type: String = "",

        @SerializedName("image")
        @Expose
        val image: String = "",

        @SerializedName("name")
        @Expose
        val name: String = "",

        @SerializedName("subLocation")
        @Expose
        val subLocation: String = "",

        @SerializedName("metaDescription")
        @Expose
        val metaDescription: String = ""
) : Visitable<PopularSearchTypeFactory> {
    override fun type(typeFactory: PopularSearchTypeFactory) = typeFactory.type(this)

    data class Response(
            @SerializedName("propertyPopular")
            @Expose
            val popularSearchList: List<PopularSearch> = listOf()
    )
}

