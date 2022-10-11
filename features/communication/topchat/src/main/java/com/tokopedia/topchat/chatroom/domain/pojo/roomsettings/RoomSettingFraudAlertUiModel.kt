package com.tokopedia.topchat.chatroom.domain.pojo.roomsettings


import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.topchat.chatroom.view.adapter.TopChatTypeFactory

data class RoomSettingFraudAlertUiModel(
        @SerializedName("enable")
        val enable: Boolean = false,
        @SerializedName("imageUrl")
        val imageUrl: String = "",
        @SerializedName("text")
        val text: String = ""
): Visitable<TopChatTypeFactory> {

        override fun type(typeFactory: TopChatTypeFactory): Int {
                return typeFactory.type(this)
        }

}