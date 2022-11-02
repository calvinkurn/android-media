package com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.scheduledelivery

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class ShippingMetadata(
    @SerializedName("sla_delivery")
    val slaDelivery: SLADelivery = SLADelivery()
) : Parcelable
