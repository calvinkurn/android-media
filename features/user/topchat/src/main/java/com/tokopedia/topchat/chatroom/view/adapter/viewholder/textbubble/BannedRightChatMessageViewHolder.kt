package com.tokopedia.topchat.chatroom.view.adapter.viewholder.textbubble

import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.chat_common.data.MessageViewModel
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ChatLinkHandlerListener
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.AdapterListener
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.CommonViewHolderListener
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.binder.ChatMessageViewHolderBinder
import com.tokopedia.topchat.common.util.ViewUtil

class BannedRightChatMessageViewHolder(
        itemView: View?,
        listener: ChatLinkHandlerListener,
        commonListener: CommonViewHolderListener,
        adapterListener: AdapterListener
) : RightChatMessageViewHolder(
        itemView,
        listener,
        commonListener,
        adapterListener
) {

    private var container: ConstraintLayout? = itemView?.findViewById(R.id.cl_ban_container)
    private var checkMark: ImageView? = itemView?.findViewById(R.id.ivCheckMark)
    private var hourTime: TextView? = itemView?.findViewById(R.id.tvTime)
    private var info: TextView? = itemView?.findViewById(R.id.txt_info)

    override val bg: Drawable?
        get() = ViewUtil.generateBackgroundWithShadow(
                container,
                com.tokopedia.unifyprinciples.R.color.Unify_G200,
                R.dimen.dp_topchat_20,
                R.dimen.dp_topchat_0,
                R.dimen.dp_topchat_20,
                R.dimen.dp_topchat_20,
                com.tokopedia.unifyprinciples.R.color.Unify_N700_20,
                R.dimen.dp_topchat_2,
                R.dimen.dp_topchat_1,
                Gravity.CENTER
        )

    override fun bind(message: MessageViewModel) {
        verifyReplyTime(message)
        bindMargin(message)
        bindClick()
        bindClickInfo()
        bindHeader(message)
        bindBackground(message)
        bindCheckMark(message)
        ChatMessageViewHolderBinder.bindHourTextView(message, hourTime)
    }

    private fun bindClickInfo() {
        info?.setOnClickListener {
            listener.onGoToWebView(tnc, tnc)
        }
    }

    private fun bindCheckMark(message: MessageViewModel) {
        checkMark?.let {
            ChatMessageViewHolderBinder.bindChatReadStatus(message, it)
        }
    }

    override fun bindBackground(message: MessageViewModel) {
        container?.background = bg
    }

    companion object {
        val LAYOUT = R.layout.item_topchat_chat_right_banned
        private const val tnc = "https://www.tokopedia.com/terms#konten"
    }
}