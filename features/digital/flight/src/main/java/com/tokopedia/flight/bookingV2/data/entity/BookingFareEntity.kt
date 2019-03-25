package com.tokopedia.flight.bookingV2.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by furqan on 05/03/19
 */
class BookingFareEntity (
        @SerializedName("adult")
        @Expose
        var adult: String,
        @SerializedName("child")
        @Expose
        var child: String,
        @SerializedName("infant")
        @Expose
        var infant: String,
        @SerializedName("total")
        @Expose
        var total: String,
        @SerializedName("adult_numeric")
        @Expose
        var adultNumeric: Long,
        @SerializedName("child_numeric")
        @Expose
        var childNumeric: Long,
        @SerializedName("infant_numeric")
        @Expose
        var infantNumeric: Long,
        @SerializedName("total_numeric")
        @Expose
        var totalNumeric: Long
)