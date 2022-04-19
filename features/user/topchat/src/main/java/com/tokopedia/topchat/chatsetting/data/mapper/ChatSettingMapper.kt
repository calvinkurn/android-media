package com.tokopedia.topchat.chatsetting.data.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatsetting.data.ChatGearChatList
import com.tokopedia.topchat.chatsetting.data.ChatGearChatListResponse
import com.tokopedia.topchat.chatsetting.data.uimodel.ChatSettingDividerUiModel
import com.tokopedia.topchat.chatsetting.data.uimodel.ChatSettingTitleUiModel
import com.tokopedia.topchat.chatsetting.view.adapter.ChatSettingTypeFactory
import javax.inject.Inject

class ChatSettingMapper @Inject constructor() {

    fun mapGearChatResponse(response: ChatGearChatListResponse): List<Visitable<ChatSettingTypeFactory>> {
        val gearChat = response.chatGearChatList
        val visitables = arrayListOf<Visitable<ChatSettingTypeFactory>>()
        val needSectionTitle = isNeedSectionTitle(gearChat)

        // buyer sections
        if (needSectionTitle) {
            visitables.add(ChatSettingTitleUiModel(R.string.title_topchat_as_buyer, R.drawable.ic_topchat_account_green))
        }
        if (visitables.addAll(gearChat.listBuyer)) {
            visitables.add(ChatSettingDividerUiModel())
        }

        // seller sections
        if (needSectionTitle) {
            visitables.add(ChatSettingTitleUiModel(R.string.title_topchat_as_seller, R.drawable.ic_topchat_shop_green))
        }
        if (visitables.addAll(gearChat.listSeller)) {
            visitables.add(ChatSettingDividerUiModel())
        }

        // util sections
        visitables.addAll(gearChat.listUtils)
        return visitables
    }

    private fun isNeedSectionTitle(gearChat: ChatGearChatList): Boolean {
        return gearChat.listBuyer.isNotEmpty() && gearChat.listSeller.isNotEmpty()
    }

}