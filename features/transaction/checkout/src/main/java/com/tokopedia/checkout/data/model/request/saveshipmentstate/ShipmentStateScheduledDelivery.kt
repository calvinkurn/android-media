package com.tokopedia.checkout.data.model.request.saveshipmentstate

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Created by victor on 10/10/22
 */

@Parcelize
data class ShipmentStateScheduledDelivery(
    @SerializedName("timeslot_id")
    var timeslotId: Long = 0,
    @SerializedName("scheduled_dates")
    var scheduledDates: String = "",
) : Parcelable
