package com.tokopedia.flight.bookingV2.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.flight.booking.data.cloud.entity.Amenity
import com.tokopedia.flight.orderlist.data.cloud.entity.PaymentInfoEntity

/**
 * @author by furqan on 05/03/19
 */
class BookingFlightEntity(
        @SerializedName("total_adult")
        @Expose
        var totalAdult: String = "",
        @SerializedName("total_adult_numeric")
        @Expose
        var totalAdultNumeric: Long = 0,
        @SerializedName("total_child")
        @Expose
        var totalChild: String = "",
        @SerializedName("total_child_numeric")
        @Expose
        var totalChildNumeric: Long = 0,
        @SerializedName("total_price")
        @Expose
        var totalPrice: String = "",
        @SerializedName("total_price_numeric")
        @Expose
        var totalPriceNumeric: Long = 0,
        @SerializedName("journeys")
        @Expose
        var journeys: MutableList<BookingJourneyEntity> = arrayListOf(),
        @SerializedName("payment")
        @Expose
        var payment: PaymentInfoEntity = PaymentInfoEntity(),
        @SerializedName("is_domestic")
        @Expose
        var isDomestic: Boolean = true,
        @SerializedName("reprice_time")
        @Expose
        var repriceTime: Int = 0,
        @SerializedName("adult")
        @Expose
        var adult: Int = 0,
        @SerializedName("child")
        @Expose
        var child: Int = 0,
        @SerializedName("infant")
        @Expose
        var infant: Int = 0,
        @SerializedName("class")
        @Expose
        var flightClass: Int = 1,
        @SerializedName("combo_key")
        @Expose
        var comboKey: String = "",
        @SerializedName("mandatory_dob")
        @Expose
        var mandatoryDob: Boolean = false,
        @SerializedName("amenities")
        @Expose
        var amenities: MutableList<Amenity> = arrayListOf()
)