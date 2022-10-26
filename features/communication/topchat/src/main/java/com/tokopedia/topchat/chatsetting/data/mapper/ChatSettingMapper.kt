package com.tokopedia.topchat.chatsetting.data.mapper

import android.content.Context
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatsetting.data.ChatGearChatList
import com.tokopedia.topchat.chatsetting.data.ChatGearChatListResponse
import com.tokopedia.topchat.chatsetting.data.uimodel.ChatSettingDividerUiModel
import com.tokopedia.topchat.chatsetting.data.uimodel.ChatSettingSellerUiModel
import com.tokopedia.topchat.chatsetting.data.uimodel.ChatSettingTitleUiModel
import com.tokopedia.topchat.chatsetting.view.adapter.ChatSettingTypeFactory
import com.tokopedia.topchat.common.di.qualifier.TopchatContext
import com.tokopedia.topchat.common.util.BubbleSettings
import com.tokopedia.topchat.common.util.Utils.isBubbleChatEnabled
import javax.inject.Inject

class ChatSettingMapper @Inject constructor(
    @TopchatContext private val context: Context,
) {

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
            if (isBubbleChatEnabled()) {
                visitables.add(ChatSettingSellerUiModel(
                    alias = getString(R.string.topchat_bubble_settings_title,
                        BubbleSettings.TITLE),
                    description = getString(R.string.topchat_bubble_settings_desc, BubbleSettings.DESCRIPTION),
                    label = getString(R.string.topchat_bubble_settings_label, BubbleSettings.LABEL),
                    //todo will update after reivin added the bubble applink
                    link = ""
                ))
            }
            visitables.add(ChatSettingDividerUiModel())
        }

        // util sections
        visitables.addAll(gearChat.listUtils)
        return visitables
    }

    private fun isNeedSectionTitle(gearChat: ChatGearChatList): Boolean {
        return gearChat.listBuyer.isNotEmpty() && gearChat.listSeller.isNotEmpty()
    }

    private fun getString(stringResources: Int, defaultValue: String): String {
        return try {
            context.getString(stringResources)
        } catch (e: Exception) {
            defaultValue
        }
    }
}
