package com.tokopedia.chatbot.view.adapter.viewholder

import android.view.View
import android.widget.TextView

import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.chat_common.util.ChatLinkHandlerMovementMethod
import com.tokopedia.chat_common.view.adapter.viewholder.BaseChatViewHolder
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ChatLinkHandlerListener
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.data.quickreply.QuickReplyListViewModel

/**
 * @author by nisie on 5/8/18.
 */
class QuickReplyViewHolder(itemView: View, private val chatLinkHandlerListener: ChatLinkHandlerListener) : BaseChatViewHolder<QuickReplyListViewModel>(itemView) {

    private val message: TextView

    init {
        message = itemView.findViewById<View>(R.id.message) as TextView
    }

    override fun bind(element: QuickReplyListViewModel) {
        super.bind(element)
        setMessage(element)
        setClickableUrl()
    }

    private fun setMessage(element: QuickReplyListViewModel) {
        if (!element.message.isEmpty()) {
            message.text = MethodChecker.fromHtml(element.message)
        }
    }

    private fun setClickableUrl() {
        message.movementMethod = ChatLinkHandlerMovementMethod(chatLinkHandlerListener)
    }

    companion object {

        val LAYOUT = R.layout.quick_reply_chat_layout
    }
}
