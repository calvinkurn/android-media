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
    val id: String = "0",

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
    val hotelCount: Int = 0,

    @SerializedName("searchType")
    @Expose
    val searchType: String = "",

    @SerializedName("searchID")
    @Expose
    val searchId: String = ""
) : Visitable<SearchDestinationTypeFactory> {
    // Need to put here since this class is actually data model, but also used as ui model
    var source: String = ""
    override fun type(typeFactory: SearchDestinationTypeFactory) = typeFactory.type(this)
}
