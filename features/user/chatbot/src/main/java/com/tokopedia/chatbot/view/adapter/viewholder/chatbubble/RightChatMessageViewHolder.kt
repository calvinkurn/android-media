package com.tokopedia.chatbot.view.adapter.viewholder.chatbubble

import android.view.Gravity
import android.view.View
import com.tokopedia.chat_common.data.MessageUiModel
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ChatLinkHandlerListener
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.util.ViewUtil
import com.tokopedia.chatbot.view.adapter.viewholder.binder.ChatbotMessageViewHolderBinder
import com.tokopedia.chatbot.view.customview.reply.ReplyBubbleAreaMessage
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toDp

class RightChatMessageViewHolder(
        itemView: View?,
        listener: ChatLinkHandlerListener,
        replyBubbleListener: ReplyBubbleAreaMessage.Listener
) : CustomChatbotMessageViewHolder(itemView, listener, replyBubbleListener) {

    private val replyBubbleArea = itemView?.findViewById<ReplyBubbleAreaMessage>(R.id.reply)

     val bg = ViewUtil.generateBackgroundWithShadow(
            customChatLayout,
            com.tokopedia.unifyprinciples.R.color.Unify_G200,
            R.dimen.dp_chatbot_20,
            R.dimen.dp_chatbot_0,
            R.dimen.dp_chatbot_20,
            R.dimen.dp_chatbot_20,
            com.tokopedia.unifyprinciples.R.color.Unify_N700_20,
            R.dimen.dp_chatbot_2,
            R.dimen.dp_chatbot_1,
            Gravity.CENTER
    )

    val bgForChatReplyBubble = ViewUtil.generateBackgroundWithShadow(
        customChatLayout,
        com.tokopedia.unifyprinciples.R.color.Unify_GN50,
        R.dimen.dp_chatbot_20,
        R.dimen.dp_chatbot_0,
        R.dimen.dp_chatbot_20,
        R.dimen.dp_chatbot_20,
        com.tokopedia.unifyprinciples.R.color.Unify_N700_20,
        R.dimen.dp_chatbot_2,
        R.dimen.dp_chatbot_1,
        Gravity.CENTER
    )

    override fun bind(message: MessageUiModel) {
        super.bind(message)
        ChatbotMessageViewHolderBinder.bindChatReadStatus(message, customChatLayout)
        bindBackground(message)
//        if (message.parentReply!=null){
        //    replyBubbleArea?.composeMsg(message.parentReply?.name, message.parentReply?.mainText)
     //       replyBubbleArea?.background = bg
            customChatLayout?.background = bgForChatReplyBubble
            replyBubbleArea?.updateReplyButtonState(false)
            replyBubbleArea?.updateBackground(false)
            replyBubbleArea?.setPadding(replyBubbleArea?.paddingLeft,replyBubbleArea?.paddingTop,replyBubbleArea?.paddingRight,50F.toDp().toInt())
            replyBubbleArea?.show()
//        }else{
//            replyBubbleArea?.hide()
//        }
    }

    private fun bindBackground(message: MessageUiModel) {
        customChatLayout?.background = bg
        replyBubbleArea?.updateBackground(false)
    }

    override fun getChatStatusId(): Int {
        return R.id.ivCheckMark
    }

    override fun alwaysShowTime(): Boolean {
        return true
    }

    companion object {
        val LAYOUT = R.layout.item_chatbot_chat_right
    }
}