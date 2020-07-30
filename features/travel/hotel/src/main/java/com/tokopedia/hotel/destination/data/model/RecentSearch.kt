package com.tokopedia.hotel.destination.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by jessica on 25/03/19
 */

data class RecentSearch(
        @SerializedName("UUID")
    @Expose
    val uuid: String = "",

        @SerializedName("property")
    @Expose
    val property: Property = Property(),

        @SerializedName("startTime")
    @Expose
    val startTime: String = "",

        @SerializedName("endTime")
    @Expose
    val endTime: String = "",

        @SerializedName("lastSearch")
    @Expose
    val lastSearch: String = "",

        @SerializedName("customer")
    @Expose
    val customer: Customer = Customer()

) {
    data class Property(
            @SerializedName("type")
            @Expose
            val type: String = "",

            @SerializedName("value")
            @Expose
            val value: String = "",

            @SerializedName("ID")
            @Expose
            val id: String = "",

            @SerializedName("location")
            @Expose
            val location: Location = Location()
    ) {
        data class Location (
                 @SerializedName("district")
                 @Expose
                 val district: String = "",

                 @SerializedName("region")
                 @Expose
                 val region: String = "",

                 @SerializedName("city")
                 @Expose
                 val city: String = "",

                 @SerializedName("country")
                 @Expose
                 val country: String = ""
        )
    }

    data class Customer(
            @SerializedName("adult")
            @Expose
            val adult: Int = 0,

            @SerializedName("child")
            @Expose
            val child: Int = 0,

            @SerializedName("class")
            @Expose
            val customerClass: String = "",

            @SerializedName("infant")
            @Expose
            val infant: Int = 0,

            @SerializedName("room")
            @Expose
            val room: String = ""
    )

    data class Response(
            @SerializedName("travelRecentSearch")
            @Expose
            val recentSearch: List<RecentSearch> = listOf()
    )

    data class DeleteResponse(
            @SerializedName("travelRecentSearchDelete")
            @Expose
            val travelRecentSearchDelete: DeleteResult = DeleteResult()
    ) {
        data class DeleteResult(
                @SerializedName("Result")
                @Expose
                val result: Boolean = false
        )
    }
}