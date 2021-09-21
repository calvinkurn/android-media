package com.tokopedia.hotel.search_map.data.model.params

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ParamFilter(
        @SerializedName("maxPrice")
        @Expose
        var maxPrice: Int = 0,

        @SerializedName("minPrice")
        @Expose
        var minPrice: Int = 0,

        @SerializedName("star")
        @Expose
        var star: List<Int> = listOf(),

        @SerializedName("paymentType")
        @Expose
        var paymentType: Int = 0,

        @SerializedName("mealPlan")
        @Expose
        var mealPlan: Int = 0,

        @SerializedName("reviewScore")
        @Expose
        var reviewScore: Int = 0,

        @SerializedName("hotelFacilities")
        @Expose
        var hotelFacilities: List<Int> = listOf(),

        @SerializedName("roomFacilities")
        @Expose
        var roomFacilities: List<Int> = listOf(),

        @SerializedName("dealType")
        @Expose
        var dealType: List<Int> = listOf(),

        @SerializedName("platformType")
        @Expose
        var platformType: Int = 0,

        @SerializedName("propertyType")
        @Expose
        var propertyType: List<Int> = listOf(),

        @SerializedName("cancellationPolicy")
        @Expose
        var cancellationPolicies: List<Int> = listOf()
)