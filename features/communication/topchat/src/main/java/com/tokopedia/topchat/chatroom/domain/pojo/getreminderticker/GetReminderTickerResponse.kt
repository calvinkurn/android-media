package com.tokopedia.topchat.chatroom.domain.pojo.getreminderticker


import com.google.gson.annotations.SerializedName
import com.tokopedia.topchat.chatroom.view.uimodel.ReminderTickerUiModel

data class GetReminderTickerResponse(
    @SerializedName("GetReminderTicker")
    var getReminderTicker: ReminderTickerUiModel = ReminderTickerUiModel()
)