package com.tokopedia.chatbot.view.adapter.viewholder.chatbubble

import android.view.Gravity
import android.view.View
import com.google.gson.GsonBuilder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.chat_common.data.MessageViewModel
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ChatLinkHandlerListener
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.domain.pojo.SenderInfoData
import com.tokopedia.chatbot.util.ViewUtil
import com.tokopedia.chatbot.view.adapter.viewholder.listener.ChatbotAdapterListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

class LeftChatMessageViewHolder(
        itemView: View?,
        listener: ChatLinkHandlerListener,
        private val chatbotAdapterListener: ChatbotAdapterListener,
) : CustomChatbotMessageViewHolder(itemView, listener) {

    private val senderAvatar = itemView?.findViewById<ImageUnify>(R.id.senderAvatar)
    private val senderName = itemView?.findViewById<Typography>(R.id.senderName)

    private val bg = ViewUtil.generateBackgroundWithShadow(
            customChatLayout,
            com.tokopedia.unifyprinciples.R.color.Unify_N0,
            R.dimen.dp_topchat_0,
            R.dimen.dp_topchat_20,
            R.dimen.dp_topchat_20,
            R.dimen.dp_topchat_20,
            com.tokopedia.unifyprinciples.R.color.Unify_N700_20,
            R.dimen.dp_topchat_2,
            R.dimen.dp_topchat_1,
            Gravity.CENTER
    )

    override fun bind(message: MessageViewModel) {
        super.bind(message)
        hideSenderInfo()
        val senderInfoData = convertToSenderInfo(message.source)
        bindBackground(message)
        if (chatbotAdapterListener.isPreviousItemSender(adapterPosition)) {
            senderInfoData?.let { bindSenderInfo(it) }
        }
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
        if (source.isNotEmpty() && source.startsWith("chatbot")) {
            val s = source.substring(8, source.length)
            return GsonBuilder().create()
                    .fromJson<SenderInfoData>(s,
                            SenderInfoData::class.java)
        } else return null
    }

    private fun bindBackground(message: MessageViewModel) {
        customChatLayout?.background = bg
    }

    private fun bindMessageInfo(message: MessageViewModel) {
        if (!message.isSender) {
            customChatLayout?.showInfo()
        } else {
            customChatLayout?.hideInfo()
        }
    }

    companion object {
        val LAYOUT = R.layout.item_chatbot_chat_left
    }
}