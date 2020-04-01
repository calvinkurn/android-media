package com.tokopedia.topchat.chatsetting.view.adapter

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.topchat.chatsetting.data.ChatSetting

interface ChatSettingTypeFactory: AdapterTypeFactory {
    fun type(chatSetting: ChatSetting): Int
}