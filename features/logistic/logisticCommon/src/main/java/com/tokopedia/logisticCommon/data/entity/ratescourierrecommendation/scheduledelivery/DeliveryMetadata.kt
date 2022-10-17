package com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.scheduledelivery

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class DeliveryMetadata(
    @SerializedName("courier_name")
    val courierName: String = "",
    @SerializedName("eta_text")
    val etaText: String = "",
    @SerializedName("delivery_time_start")
    val deliveryTimeStart: String = "",
    @SerializedName("delivery_time_end")
    val deliveryTimeEnd: String = "",
) : Parcelable
