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
    var featureId: Long = 0,
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
    var localId : String = ""

    override fun type(typeFactory: TopChatTypeFactory): Int {
        return typeFactory.type(this)
    }

    fun getTickerFeature(): String {
        return when (featureId) {
            FEATURE_ID_SRW -> FEATURE_SRW
            FEATURE_ID_FRAUD -> FEATURE_FRAUD
            else -> ""
        }
    }

    companion object {
        private const val FEATURE_ID_SRW: Long = 1
        private const val FEATURE_SRW = "srw_reminder"
        private const val FEATURE_ID_FRAUD: Long = 2
        private const val FEATURE_FRAUD = "fraud"

        fun mapToReminderTickerUiModel(reminderPojo: TickerReminderPojo): ReminderTickerUiModel {
            return ReminderTickerUiModel(
                isEnable = reminderPojo.isEnable?: false,
                isEnableClose = reminderPojo.isEnableClose?: false,
                featureId = reminderPojo.featureId.orZero(),
                mainText = reminderPojo.mainText?: "",
                regexMessage = reminderPojo.regexMessage?: "",
                subText = reminderPojo.subText?: "",
                url = reminderPojo.url?: "",
                urlLabel = reminderPojo.urlLabel?: "",
                tickerType = reminderPojo.tickerType?: ""
            ).apply {
                localId = reminderPojo.localId?: ""
            }
        }
    }
}