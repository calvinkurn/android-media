package com.tokopedia.topchat.chatsetting.data

import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.topchat.chatsetting.view.adapter.ChatSettingTypeFactory

data class ChatSetting(
        @SerializedName("alias")
        val alias: String = "",
        @SerializedName("link")
        val link: String = ""
): Visitable<ChatSettingTypeFactory> {

        override fun type(typeFactory: ChatSettingTypeFactory): Int {
                return typeFactory.type(this)
        }

}