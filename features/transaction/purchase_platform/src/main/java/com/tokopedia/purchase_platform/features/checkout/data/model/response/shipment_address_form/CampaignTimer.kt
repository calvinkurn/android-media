package com.tokopedia.purchase_platform.features.checkout.data.model.response.shipment_address_form
import com.google.gson.annotations.SerializedName


data class CampaignTimer(
    @SerializedName("description")
    var description: String = "",
    @SerializedName("expired_timer_message")
    var expiredTimerMessage: ExpiredTimerMessage = ExpiredTimerMessage(),
    @SerializedName("show_timer")
    var showTimer: Boolean = false,
    @SerializedName("timer_detail")
    var timerDetail: TimerDetail = TimerDetail()
)

data class ExpiredTimerMessage(
    @SerializedName("button")
    var button: String = "",
    @SerializedName("description")
    var description: String = "",
    @SerializedName("title")
    var title: String = ""
)

data class TimerDetail(
    @SerializedName("deduct_time")
    var deductTime: String = "",
    @SerializedName("expired_duration")
    var expiredDuration: Int = 0,
    @SerializedName("expired_time")
    var expiredTime: String = ""
)