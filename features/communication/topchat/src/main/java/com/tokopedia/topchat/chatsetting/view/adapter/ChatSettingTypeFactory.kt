package com.tokopedia.topchat.chatsetting.view.adapter

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.topchat.chatsetting.data.uimodel.ChatSettingDividerUiModel
import com.tokopedia.topchat.chatsetting.data.uimodel.ChatSettingTitleUiModel
import com.tokopedia.topchat.chatsetting.data.uimodel.ItemChatSettingUiModel

interface ChatSettingTypeFactory : AdapterTypeFactory {
    fun type(chatSettingDividerUiModel: ChatSettingDividerUiModel): Int
    fun type(chatSettingTitleUiModel: ChatSettingTitleUiModel): Int
    fun type(itemChatSettingUiModel: ItemChatSettingUiModel): Int
}