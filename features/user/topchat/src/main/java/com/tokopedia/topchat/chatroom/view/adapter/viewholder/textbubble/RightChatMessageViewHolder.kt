package com.tokopedia.topchat.chatroom.view.adapter.viewholder.textbubble

import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.tokopedia.chat_common.data.MessageViewModel
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ChatLinkHandlerListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.AdapterListener
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.CommonViewHolderListener
import com.tokopedia.topchat.common.util.ViewUtil
import com.tokopedia.unifyprinciples.Typography

open class RightChatMessageViewHolder constructor(
        itemView: View?,
        listener: ChatLinkHandlerListener,
        private val commonListener: CommonViewHolderListener,
        private val adapterListener: AdapterListener
) : ChatMessageViewHolder(itemView, listener, adapterListener) {

    var header: LinearLayout? = itemView?.findViewById(R.id.llRoleUser)
    var headerRole: Typography? = itemView?.findViewById(R.id.tvRole)
    var smartReplyBlueDot: ImageView? = itemView?.findViewById(R.id.img_sr_blue_dot)
    private val bg = ViewUtil.generateBackgroundWithShadow(
            itemView,
            R.color.bg_topchat_right_message,
            R.dimen.dp_topchat_20,
            R.dimen.dp_topchat_0,
            R.dimen.dp_topchat_20,
            R.dimen.dp_topchat_20,
            R.color.topchat_message_shadow,
            R.dimen.dp_topchat_2,
            R.dimen.dp_topchat_1,
            Gravity.CENTER
    )

    override fun bind(message: MessageViewModel) {
        super.bind(message)
        bindChatReadStatus(message)
        bindHeader(message)
        bindBackground(message)
    }

    private fun bindBackground(message: MessageViewModel) {
        fxChat?.background = bg
    }

    private fun bindHeader(message: MessageViewModel) {
        if (
                (message.isFromAutoReply() || message.isFromSmartReply()) &&
                message.isSender &&
                commonListener.isSeller()
        ) {
            val headerRoleText = if (message.isFromSmartReply()) {
                itemView.context?.getString(R.string.tittle_header_smart_reply)
            } else {
                itemView.context?.getString(R.string.tittle_header_auto_reply)
            } ?: ""
            bindBlueDot(message)
            header?.show()
            headerRole?.show()
            headerRole?.text = headerRoleText
        } else {
            header?.hide()
        }
    }

    private fun bindBlueDot(message: MessageViewModel) {
        if (message.isFromSmartReply()) {
            smartReplyBlueDot?.show()
        } else {
            smartReplyBlueDot?.hide()
        }
    }

    override fun getChatStatusId(): Int {
        return R.id.ivCheckMark
    }

    override fun alwaysShowTime(): Boolean {
        return true
    }

    companion object {
        val LAYOUT = R.layout.item_topchat_chat_right
    }
}