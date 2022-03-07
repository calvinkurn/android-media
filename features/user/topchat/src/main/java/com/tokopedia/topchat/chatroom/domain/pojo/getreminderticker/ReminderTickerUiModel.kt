package com.tokopedia.topchat.chatroom.domain.pojo.getreminderticker


import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.topchat.chatroom.view.adapter.TopChatTypeFactory

data class ReminderTickerUiModel(
    @SerializedName("enable")
    var enable: Boolean = false,
    @SerializedName("enableClose")
    var enableClose: Boolean = false,
    @SerializedName("featureId")
    var featureId: Int = 0,
    @SerializedName("mainText")
    var mainText: String = "",
    @SerializedName("regexMessage")
    var regexMessage: String = "",
    @SerializedName("subText")
    var subText: String = "",
    @SerializedName("url")
    var url: String = "",
    @SerializedName("urlLabel")
    var urlLabel: String = ""
) : Visitable<TopChatTypeFactory> {
    val impressHolder = ImpressHolder()
    override fun type(typeFactory: TopChatTypeFactory): Int {
        return typeFactory.type(this)
    }
}