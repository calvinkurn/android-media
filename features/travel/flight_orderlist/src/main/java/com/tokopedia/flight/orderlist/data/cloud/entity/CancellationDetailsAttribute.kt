package com.tokopedia.flight.orderlist.data.cloud.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by furqan on 30/04/18.
 */

class CancellationDetailsAttribute(
        @SerializedName("journey_id")
        @Expose
        var journeyId: Long = 0,
        @SerializedName("passenger_id")
        @Expose
        val passengerId: String = "") {

    override fun equals(obj: Any?): Boolean {
        var isEqual = false

        if (obj != null && obj is CancellationDetailsAttribute) {
            isEqual = this.passengerId == obj.passengerId
        }

        if (obj != null && obj is String) {
            isEqual = this.passengerId == obj
        }

        return isEqual
    }
}
