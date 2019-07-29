package com.tokopedia.hotel.search.data.model.params

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ParamLocation(
        @SerializedName("cityID")
        @Expose
        var cityID: Int = 0,

        @SerializedName("districtID")
        @Expose
        var districtID: Int = 0,

        @SerializedName("regionID")
        @Expose
        var regionID: Int = 0,

        @SerializedName("latitude")
        @Expose
        var latitude: Float = 0f,

        @SerializedName("longitude")
        @Expose
        var longitude: Float = 0f
)