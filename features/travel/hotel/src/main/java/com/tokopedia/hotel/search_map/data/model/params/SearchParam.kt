package com.tokopedia.hotel.search_map.data.model.params

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SearchParam(
        @SerializedName("location")
        @Expose
        var location: ParamLocation = ParamLocation(),

        @SerializedName("filter")
        @Expose
        var filter: ParamFilter = ParamFilter(),

        @SerializedName("filters")
        @Expose
        var filters: MutableList<ParamFilterV2> = mutableListOf(),

        @SerializedName("sort")
        @Expose
        var sort: ParamSort = ParamSort(),

        @SerializedName("checkIn")
        @Expose
        var checkIn: String = "",

        @SerializedName("checkOut")
        @Expose
        var checkOut: String = "",

        @SerializedName("room")
        @Expose
        var room: Int = 0,

        @SerializedName("guest")
        @Expose
        var guest: ParamGuest = ParamGuest(),

        @SerializedName("page")
        @Expose
        var page: Int = 1,

        @SerializedName("language")
        @Expose
        var language: String = "ID"
        )