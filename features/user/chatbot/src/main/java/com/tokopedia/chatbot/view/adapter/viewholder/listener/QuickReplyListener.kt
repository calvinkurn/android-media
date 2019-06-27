package com.tokopedia.chatbot.view.adapter.viewholder.listener

import com.tokopedia.chatbot.data.quickreply.QuickReplyListViewModel
import com.tokopedia.chatbot.data.quickreply.QuickReplyViewModel

/**
 * @author by nisie on 06/12/18.
 */
interface QuickReplyListener{
    fun onQuickReplyClicked(quickReplyListViewModel: QuickReplyListViewModel, model: QuickReplyViewModel)
}