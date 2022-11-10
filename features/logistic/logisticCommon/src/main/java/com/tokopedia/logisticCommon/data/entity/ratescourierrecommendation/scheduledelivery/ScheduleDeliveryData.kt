package com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.scheduledelivery

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
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
    @SerializedName("ticker")
    val ticker: Ticker = Ticker(),
    @SerializedName("labels")
    val labels: List<String> = arrayListOf(),
    @SerializedName("error")
    val error: Error = Error(),
    @SerializedName("delivery_services")
    val deliveryServices: List<DeliveryService> = arrayListOf(),
) : Parcelable
