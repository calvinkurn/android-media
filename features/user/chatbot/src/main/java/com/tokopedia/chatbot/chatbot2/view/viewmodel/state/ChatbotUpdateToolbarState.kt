package com.tokopedia.chatbot.chatbot2.view.viewmodel.state

import com.tokopedia.chatbot.data.toolbarpojo.ToolbarAttributes

sealed class ChatbotUpdateToolbarState {
    data class UpdateToolbar(
        val profileName: String?,
        val profileImage: String?,
        val badgeImage: ToolbarAttributes.BadgeImage?
    ) : ChatbotUpdateToolbarState()
}
