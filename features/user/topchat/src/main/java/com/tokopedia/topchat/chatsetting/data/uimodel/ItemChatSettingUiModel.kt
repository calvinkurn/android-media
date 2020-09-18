package com.tokopedia.topchat.chatsetting.data.uimodel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.topchat.chatsetting.view.adapter.ChatSettingTypeFactory

abstract class ItemChatSettingUiModel(
        @SerializedName("alias")
        @Expose
        val alias: String = "",
        @SerializedName("description")
        @Expose
        val description: String = "",
        @SerializedName("label")
        @Expose
        val label: String = "",
        @SerializedName("link")
        @Expose
        val link: String = "",
        @SerializedName("typeLabel")
        @Expose
        val typeLabel: Int = 0
) : Visitable<ChatSettingTypeFactory> {

    override fun type(typeFactory: ChatSettingTypeFactory): Int {
        return typeFactory.type(this)
    }

}