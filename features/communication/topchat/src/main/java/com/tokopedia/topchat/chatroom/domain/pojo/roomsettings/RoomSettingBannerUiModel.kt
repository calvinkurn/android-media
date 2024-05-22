package com.tokopedia.topchat.chatroom.domain.pojo.roomsettings


import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.topchat.chatroom.view.adapter.typefactory.TopChatRoomTypeFactory

data class RoomSettingBannerUiModel(
        @SerializedName("enable")
        val enable: Boolean = false,
        @SerializedName("imageUrl")
        val imageUrl: String = "",
        @SerializedName("text")
        val text: String = "",
        @SerializedName("typeString")
        val typeString: String = ""
) : Visitable<TopChatRoomTypeFactory> {

        override fun type(typeFactory: TopChatRoomTypeFactory): Int {
                return typeFactory.type(this)
        }

        companion object {
                const val TYPE_INFO = "info"
                const val TYPE_WARNING = "warning"
                const val TYPE_ERROR = "error"
        }
}
