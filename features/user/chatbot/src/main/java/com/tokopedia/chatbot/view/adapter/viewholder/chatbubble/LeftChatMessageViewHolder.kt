package com.tokopedia.chatbot.view.adapter.viewholder.chatbubble

import android.view.Gravity
import android.view.View
import com.google.gson.GsonBuilder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.chat_common.data.MessageUiModel
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ChatLinkHandlerListener
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.domain.pojo.senderinfo.SenderInfoData
import com.tokopedia.chatbot.util.ViewUtil
import com.tokopedia.chatbot.view.adapter.viewholder.listener.ChatbotAdapterListener
import com.tokopedia.chatbot.view.customview.reply.ReplyBubbleAreaMessage
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

class LeftChatMessageViewHolder(
        itemView: View?,
        listener: ChatLinkHandlerListener,
        private val chatbotAdapterListener: ChatbotAdapterListener,
        replyBubbleListener: ReplyBubbleAreaMessage.Listener
) : CustomChatbotMessageViewHolder(itemView, listener,replyBubbleListener) {

    private val senderAvatar = itemView?.findViewById<ImageUnify>(R.id.senderAvatar)
    private val senderName = itemView?.findViewById<Typography>(R.id.senderName)
    private val replyBubbleArea = itemView?.findViewById<ReplyBubbleAreaMessage>(R.id.reply)

    private val bg = ViewUtil.generateBackgroundWithShadow(
            customChatLayout,
            com.tokopedia.unifyprinciples.R.color.Unify_N0,
            R.dimen.dp_chatbot_0,
            R.dimen.dp_chatbot_20,
            R.dimen.dp_chatbot_20,
            R.dimen.dp_chatbot_20,
            com.tokopedia.unifyprinciples.R.color.Unify_N700_20,
            R.dimen.dp_chatbot_2,
            R.dimen.dp_chatbot_1,
            Gravity.CENTER
    )

    override fun bind(message: MessageUiModel) {
        super.bind(message)
        hideSenderInfo()
        val senderInfoData = convertToSenderInfo(message.source)
        bindBackground(message)
        if (chatbotAdapterListener.isPreviousItemSender(adapterPosition)) {
            senderInfoData?.let { bindSenderInfo(it) }
        }

        if (message.parentReply!=null){
            replyBubbleArea?.composeMsg(message.parentReply?.name, message.parentReply?.mainText)
            replyBubbleArea?.show()
        }else{
            replyBubbleArea?.hide()
        }

    }

    private fun bindReplyBubbleListener() {
        replyBubbleArea?.setReplyListener(replyBubbleListener)
    }

    private fun bindReplyReference(msg: MessageUiModel) {
        replyBubbleArea?.bindReplyData(msg)
    }

    private fun hideSenderInfo() {
        senderAvatar?.hide()
        senderName?.hide()
    }

    private fun bindSenderInfo(senderInfoData: SenderInfoData) {
        senderAvatar?.show()
        senderName?.show()
        ImageHandler.loadImageCircle2(itemView.context, senderAvatar, senderInfoData.iconUrl)
        senderName?.text = senderInfoData.name
    }

    private fun convertToSenderInfo(source: String): SenderInfoData? {
        val senderInfoPrefix = itemView.context.getString(R.string.chatbot_sender_info_prefix)
        if (source.isNotEmpty() && source.startsWith(senderInfoPrefix)) {
            val s = source.substring(senderInfoPrefix.length, source.length)
            return GsonBuilder().create()
                    .fromJson<SenderInfoData>(s,
                            SenderInfoData::class.java)
        } else return null
    }

    private fun bindBackground(message: MessageUiModel) {
        customChatLayout?.background = bg
        replyBubbleArea?.updateBackground(true)
    }

    private fun bindMessageInfo(message: MessageUiModel) {
        if (!message.isSender) {
            customChatLayout?.showReadMoreView()
        } else {
            customChatLayout?.hideReadMoreView()
        }
    }

    override fun getDateContainerId(): Int = R.id.dateContainer

    companion object {
        val LAYOUT = R.layout.item_chatbot_chat_left
    }
}