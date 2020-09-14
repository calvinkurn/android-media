package com.tokopedia.topchat.chatsetting.data.uimodel

import androidx.annotation.DrawableRes
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.topchat.chatsetting.view.adapter.ChatSettingTypeFactory

data class ChatSettingTitleUiModel(
        val title: String,
        @DrawableRes val icon: Int
) : Visitable<ChatSettingTypeFactory> {

    override fun type(typeFactory: ChatSettingTypeFactory): Int {
        return typeFactory.type(this)
    }

}