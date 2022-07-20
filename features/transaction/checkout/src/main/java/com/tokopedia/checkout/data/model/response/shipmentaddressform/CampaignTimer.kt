package com.tokopedia.checkout.data.model.response.shipmentaddressform

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
        @SerializedName("Button")
        var button: String = "",
        @SerializedName("description")
        var description: String = "",
        @SerializedName("title")
        var title: String = ""
)

data class TimerDetail(
        @SerializedName("deduct_time")
        var deductTime: String = "",
        @SerializedName("expire_duration")
        var expiredDuration: Int = 0,
        @SerializedName("expired_time")
        var expiredTime: String = "",
        @SerializedName("server_time")
        var serverTime: String = ""
)