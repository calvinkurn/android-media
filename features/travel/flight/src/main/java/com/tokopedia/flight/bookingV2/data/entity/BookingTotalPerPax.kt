package com.tokopedia.flight.bookingV2.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by furqan on 05/03/19
 */
class BookingTotalPerPax (
        @SerializedName("adult_before_total")
        @Expose
        var adultBeforeTotal: Long,
        @SerializedName("adult_total")
        @Expose
        var adultTotal: Long,
        @SerializedName("child_before_total")
        @Expose
        var childBeforeTotal: Long,
        @SerializedName("child_total")
        @Expose
        var childTotal: Long,
        @SerializedName("infant_before_total")
        @Expose
        var infantBeforeTotal: Long,
        @SerializedName("infant_total")
        @Expose
        var infantTotal: Long
)