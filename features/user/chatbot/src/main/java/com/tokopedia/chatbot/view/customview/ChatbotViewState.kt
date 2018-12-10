package com.tokopedia.chatbot.view.customview

import android.support.v7.widget.RecyclerView
import android.view.View
import com.tokopedia.chat_common.view.BaseChatViewState
import com.tokopedia.chatbot.view.adapter.ChatbotAdapter
import com.tokopedia.chatbot.view.adapter.QuickReplyAdapter

/**
 * @author by nisie on 07/12/18.
 */
class ChatbotViewState(override var view: View) : BaseChatViewState(view){


    private lateinit var adapter: ChatbotAdapter
    private lateinit var quickReplyAdapter: QuickReplyAdapter
    private lateinit var rvQuickReply: RecyclerView

    private lateinit var reasonBottomSheet: ReasonBottomSheet

    fun showQuickReply() {

    }

    fun hasQuickReply(): Boolean {
        return quickReplyAdapter != null && rvQuickReply!= null
    }

    fun hideQuickReply() {
        quickReplyAdapter.clearData()
        rvQuickReply.visibility = View.GONE
    }

}