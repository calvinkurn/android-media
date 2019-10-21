package com.tokopedia.hotel.destination.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.hotel.destination.view.adapter.SearchDestinationTypeFactory

/**
 * @author by jessica on 27/03/19
 */

data class SearchDestination(
        @SerializedName("id")
        @Expose
        val id: Int = 0,

        @SerializedName("type")
        @Expose
        val type: String = "",

        @SerializedName("typeID")
        @Expose
        val typeID: String = "",

        @SerializedName("tag")
        @Expose
        val tag: String = "",

        @SerializedName("icon")
        @Expose
        val icon: String = "",

        @SerializedName("iconURL")
        @Expose
        val iconUrl: String = "",

        @SerializedName("name")
        @Expose
        val name: String = "",

        @SerializedName("location")
        @Expose
        val location: String = "",

        @SerializedName("hotelCount")
        @Expose
        val hotelCount: Int = 0
): Visitable<SearchDestinationTypeFactory> {
    override fun type(typeFactory: SearchDestinationTypeFactory) = typeFactory.type(this)
}