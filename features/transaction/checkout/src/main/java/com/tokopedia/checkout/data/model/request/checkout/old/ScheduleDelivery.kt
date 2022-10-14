package com.tokopedia.checkout.data.model.request.checkout.old

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Created by victor on 10/10/22
 */

@Parcelize
data class ScheduleDelivery(
    @SerializedName("timeslot_id")
    var timeslotId: Long = 0L,
    @SerializedName("schedule_date")
    var scheduleDate: String = "",
    @SerializedName("shipping_price")
    var shippingPrice: Double = 0.0,
) : Parcelable
