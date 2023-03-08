package com.tokopedia.logisticcart.scheduledelivery.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.ErrorRatesDetailData
import kotlinx.parcelize.Parcelize

@Parcelize
class ScheduleDeliveryData(
    @SerializedName("rates_id")
    val ratesId: Long = 0,
    @SerializedName("available")
    val available: Boolean = false,
    @SerializedName("hidden")
    val hidden: Boolean = true,
    @SerializedName("recommend")
    val recommend: Boolean = false,
    @SerializedName("delivery_type")
    val deliveryType: Int = 0,
    @SerializedName("title")
    val title: String = "",
    @SerializedName("text")
    val text: String = "",
    @SerializedName("notice")
    val notice: Notice = Notice(),
    @SerializedName("error")
    val error: ErrorRatesDetailData = ErrorRatesDetailData(),
    @SerializedName("delivery_services")
    val deliveryServices: List<DeliveryService> = arrayListOf()
) : Parcelable
