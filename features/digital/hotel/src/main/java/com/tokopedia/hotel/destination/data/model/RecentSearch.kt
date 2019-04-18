package com.tokopedia.hotel.destination.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by jessica on 25/03/19
 */

data class RecentSearch(
    @SerializedName("destinationId")
    @Expose
    val id: Int = 0,

    @SerializedName("type")
    @Expose
    val type: String = "",

    @SerializedName("name")
    @Expose
    val name: String = "",

    @SerializedName("uuid")
    @Expose
    val uuid: String = ""
) {
    data class Response (
            @SerializedName("propertyRecentSearch")
            @Expose
            val recentSearchList: List<RecentSearch> = listOf()
    )
}