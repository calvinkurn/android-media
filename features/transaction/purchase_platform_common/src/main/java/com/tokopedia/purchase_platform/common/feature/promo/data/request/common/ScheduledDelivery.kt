package com.tokopedia.purchase_platform.common.feature.promo.data.request.common

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Created by victor on 10/10/22
 */

@Parcelize
data class ScheduledDelivery(
    @SerializedName("timeslot_id")
    var timeslotId: Long = 0,
    @SerializedName("scheduled_dates")
    var scheduledDates: String = "",
) : Parcelable {

    constructor(): this(0, "")
}
