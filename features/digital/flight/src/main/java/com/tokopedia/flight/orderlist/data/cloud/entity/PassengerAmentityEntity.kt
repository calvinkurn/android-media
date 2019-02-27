package com.tokopedia.flight.orderlist.data.cloud.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by alvarisi on 12/6/17.
 */

class PassengerAmentityEntity(
        @SerializedName("departure_id")
        @Expose
        val departureAirportId: String = "",
        @SerializedName("arrival_id")
        @Expose
        val arrivalAirportId: String = "",
        @SerializedName("amenity_type")
        @Expose
        val amenityType: Int = 0,
        @SerializedName("price")
        @Expose
        val price: String = "",
        @SerializedName("detail")
        @Expose
        val detail: String = "",
        @SerializedName("sequence")
        @Expose
        val sequence: Int = 0,
        @SerializedName("price_numeric")
        @Expose
        val priceNumeric: Int = 0)
