package com.tokopedia.flight.bookingV2.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.flight.booking.data.cloud.entity.InsuranceEntity
import com.tokopedia.flight.booking.data.cloud.entity.NewFarePrice
import com.tokopedia.flight.booking.data.cloud.entity.Voucher

/**
 * @author by furqan on 05/03/19
 */
class BookingAttributeEntity (
        @SerializedName("flight")
        @Expose
        var flight: BookingFlightEntity = BookingFlightEntity(),
        @SerializedName("voucher")
        @Expose
        var voucher: Voucher = Voucher(),
        @SerializedName("new_price")
        @Expose
        var newPrice: List<NewFarePrice> = arrayListOf(),
        @SerializedName("insurances")
        @Expose
        var insurances: List<InsuranceEntity> = arrayListOf()
)