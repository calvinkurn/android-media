package com.tokopedia.topchat.chatroom.view.uimodel


import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.domain.pojo.tickerreminder.TickerReminderPojo
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.topchat.chatroom.view.adapter.TopChatTypeFactory

data class ReminderTickerUiModel(
    @SerializedName("enable")
    var isEnable: Boolean = false,
    @SerializedName("enableClose")
    var isEnableClose: Boolean = false,
    @SerializedName("featureId")
    var featureId: String = "0",
    @SerializedName("mainText")
    var mainText: String = "",
    @SerializedName("regexMessage")
    var regexMessage: String = "",
    @SerializedName("subText")
    var subText: String = "",
    @SerializedName("url")
    var url: String = "",
    @SerializedName("urlLabel")
    var urlLabel: String = "",
    @SerializedName("replyId")
    var replyId: String = "",
    @SerializedName("tickerType")
    var tickerType: String = "",
) : Visitable<TopChatTypeFactory> {
    val impressHolder = ImpressHolder()
    override fun type(typeFactory: TopChatTypeFactory): Int {
        return typeFactory.type(this)
    }

    companion object {
        fun mapToReminderTickerUiModel(reminderPojo: TickerReminderPojo): ReminderTickerUiModel {
            return ReminderTickerUiModel(
                isEnable = reminderPojo.isEnable?: false,
                isEnableClose = reminderPojo.isEnableClose?: false,
                featureId = (reminderPojo.featureId.orZero()).toString(),
                mainText = reminderPojo.mainText?: "",
                regexMessage = reminderPojo.regexMessage?: "",
                subText = reminderPojo.subText?: "",
                url = reminderPojo.url?: "",
                urlLabel = reminderPojo.urlLabel?: "",
                replyId = reminderPojo.replyId?: "",
                tickerType = reminderPojo.tickerType?: ""
            )
        }
    }
}