package com.tokopedia.topchat.chatsetting.data.uimodel

import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.topchat.chatsetting.view.adapter.ChatSettingTypeFactory

abstract class ItemChatSettingUiModel(
        @SerializedName("alias")
        val alias: String = "",
        @SerializedName("description")
        val description: String = "",
        @SerializedName("label")
        val label: String = "",
        @SerializedName("link")
        val link: String = "",
        @SerializedName("typeLabel")
        val typeLabel: Int = 0
) : Visitable<ChatSettingTypeFactory> {

    override fun type(typeFactory: ChatSettingTypeFactory): Int {
        return typeFactory.type(this)
    }

}