package com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.scheduledelivery

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class ValidationMetadata(
    @SerializedName("timeslot_id")
    val timeslot_id: Long = 0L,
    @SerializedName("schedule_date")
    val scheduleDate: String = "",
    @SerializedName("shipping_price")
    val shippingPrice: Double = 0.0
) : Parcelable
