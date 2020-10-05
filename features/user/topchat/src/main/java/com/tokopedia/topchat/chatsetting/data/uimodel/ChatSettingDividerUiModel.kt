package com.tokopedia.topchat.chatsetting.data.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.topchat.chatsetting.view.adapter.ChatSettingTypeFactory

class ChatSettingDividerUiModel : Visitable<ChatSettingTypeFactory> {

    override fun type(typeFactory: ChatSettingTypeFactory): Int {
        return typeFactory.type(this)
    }

}