package com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.scheduledelivery

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class SLADelivery(
    @SerializedName("etd")
    val etd: Int = 0
) : Parcelable
