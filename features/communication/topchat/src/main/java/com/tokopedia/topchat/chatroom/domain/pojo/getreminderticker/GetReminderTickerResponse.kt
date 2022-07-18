package com.tokopedia.topchat.chatroom.domain.pojo.getreminderticker


import com.google.gson.annotations.SerializedName

data class GetReminderTickerResponse(
    @SerializedName("GetReminderTicker")
    var getReminderTicker: ReminderTickerUiModel = ReminderTickerUiModel()
)