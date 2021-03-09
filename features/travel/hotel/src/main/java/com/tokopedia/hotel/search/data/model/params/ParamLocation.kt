package com.tokopedia.hotel.search.data.model.params

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ParamLocation(
        @SerializedName("cityID")
        @Expose
        var cityID: Long = 0,

        @SerializedName("districtID")
        @Expose
        var districtID: Long = 0,

        @SerializedName("regionID")
        @Expose
        var regionID: Long = 0,

        @SerializedName("latitude")
        @Expose
        var latitude: Double = 0.0,

        @SerializedName("longitude")
        @Expose
        var longitude: Double = 0.0,

        @SerializedName("radius")
        @Expose
        var radius: Double = 0.0,

        @SerializedName("searchType")
        @Expose
        var searchType: String = "",

        @SerializedName("searchID")
        @Expose
        var searchId: String= ""
)