package com.tokopedia.topchat.chatroom.view.adapter.viewholder.textbubble

import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.chat_common.data.MessageUiModel
import com.tokopedia.chat_common.view.adapter.viewholder.BaseChatViewHolder
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ChatLinkHandlerListener
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.AdapterListener
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.CommonViewHolderListener
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.binder.ChatMessageViewHolderBinder
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.getOppositeMargin

class BannedChatMessageViewHolder(
    private val itemView: View?,
    private val listener: ChatLinkHandlerListener,
    private val commonListener: CommonViewHolderListener,
    private val adapterListener: AdapterListener
) : BaseChatViewHolder<MessageUiModel>(itemView) {

    private var container: ConstraintLayout? = itemView?.findViewById(R.id.cl_ban_container)
    private var checkMark: ImageView? = itemView?.findViewById(R.id.ivCheckMark)
    private var hourTime: TextView? = itemView?.findViewById(R.id.tvTime)
    private var info: TextView? = itemView?.findViewById(R.id.txt_info)
    private val msgContainer: LinearLayout? = itemView?.findViewById(
        R.id.cl_msg_container
    )

    private val bgLeft = ChatMessageViewHolderBinder.generateLeftBg(container)
    private val bgRight = ChatMessageViewHolderBinder.generateRightBg(container)

    override fun bind(message: MessageUiModel) {
        verifyReplyTime(message)
        bindMargin(message)
        bindClickInfo()
        bindBackground(message)
        bindCheckMark(message)
        bindGravity(message)
        ChatMessageViewHolderBinder.bindHourTextView(message, hourTime)
    }

    private fun bindGravity(message: MessageUiModel) {
        if (message.isSender) {
            msgContainer?.gravity = Gravity.END
        } else {
            msgContainer?.gravity = Gravity.START
        }
    }

    private fun bindClickInfo() {
        info?.setOnClickListener {
            listener.onGoToWebView(tnc, tnc)
        }
    }

    private fun bindCheckMark(message: MessageUiModel) {
        checkMark?.let {
            ChatMessageViewHolderBinder.bindChatReadStatus(message, it)
        }
    }

    private fun bindBackground(message: MessageUiModel) {
        if (message.isSender) {
            container?.background = bgRight
        } else {
            container?.background = bgLeft
        }
    }

    private fun bindMargin(message: MessageUiModel) {
        val lp = msgContainer?.layoutParams
        if (lp is RecyclerView.LayoutParams) {
            if (adapterListener.isOpposite(adapterPosition, message.isSender)) {
                msgContainer?.setMargin(
                    0,
                    getOppositeMargin(itemView.context).toInt(),
                    0,
                    0
                )
            } else {
                msgContainer?.setMargin(0, 0, 0, 0)
            }
        }
    }

    private fun verifyReplyTime(chat: MessageUiModel) {
        try {
            if (chat.replyTime.toLongOrZero() / MILISECONDS < START_YEAR) {
                chat.replyTime = (chat.replyTime.toLongOrZero() * MILISECONDS).toString()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        val LAYOUT = R.layout.item_topchat_chat_banned
        private const val tnc = "https://www.tokopedia.com/terms#konten"
    }
}