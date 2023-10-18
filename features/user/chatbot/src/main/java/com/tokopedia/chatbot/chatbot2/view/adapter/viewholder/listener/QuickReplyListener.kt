package com.tokopedia.chatbot.chatbot2.view.adapter.viewholder.listener

import com.tokopedia.chatbot.chatbot2.view.uimodel.quickreply.QuickReplyUiModel

/**
 * @author by nisie on 06/12/18.
 */
interface QuickReplyListener {
    fun onQuickReplyClicked(model: QuickReplyUiModel, isFromDynamicAttachment: Boolean = false)
}
