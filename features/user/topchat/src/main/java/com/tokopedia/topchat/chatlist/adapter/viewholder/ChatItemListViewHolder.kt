package com.tokopedia.topchat.chatlist.adapter.viewholder

import android.graphics.Typeface.ITALIC
import android.graphics.Typeface.NORMAL
import android.support.annotation.LayoutRes
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.kotlin.extensions.view.toZeroIfNull
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatlist.listener.ChatListItemListener
import com.tokopedia.topchat.chatlist.pojo.ItemChatListPojo
import com.tokopedia.unifyprinciples.Typography

/**
 * @author : Steven 2019-08-07
 */
class ChatItemListViewHolder(itemView: View, var listener: ChatListItemListener) : AbstractViewHolder<ItemChatListPojo>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_chat_list
        const val STATE_CHAT_UNREAD = 1
        const val STATE_CHAT_READ = 2


        const val PAYLOAD_READ_STATE = 8796
        const val PAYLOAD_TYPING_STATE = 3207
        const val PAYLOAD_STOP_TYPING_STATE = 5431
    }

    val userName: Typography = itemView.findViewById(R.id.user_name)
    val thumbnail: ImageView = itemView.findViewById(R.id.thumbnail)
    val message: TextView = itemView.findViewById(R.id.message)
    val unreadCounter: Typography = itemView.findViewById(R.id.unread_counter)

    override fun bind(element: ItemChatListPojo) {
        val attributes = element.attributes
        val data = attributes?.contact
        data?.let { contact ->
            itemView.setOnClickListener {
                attributes.readStatus = STATE_CHAT_READ
                bindReadState(attributes.readStatus, attributes.unreads)
                listener.chatItemClicked(element)
            }

            userName.text = contact.contactName
            ImageHandler.loadImageCircle2(itemView.context, thumbnail, contact.thumbnail)

            bindReadState(attributes.readStatus, attributes.unreads)
            bindMessageState(attributes.lastReplyMessage)
        }

    }

    override fun bind(element: ItemChatListPojo, payloads: MutableList<Any>) {
        super.bind(element, payloads)

        if (payloads.isEmpty() || payloads[0] !is Int) {
            return
        }

        when (payloads[0] as Int) {
            PAYLOAD_READ_STATE -> bindReadState(element.attributes?.readStatus, element.attributes?.unreads)
            PAYLOAD_TYPING_STATE -> bindTypingState()
            PAYLOAD_STOP_TYPING_STATE -> bindMessageState(element.attributes?.lastReplyMessage.toBlankOrString())
            else -> bind(element)
        }
    }

    private fun bindTypingState() {
        message.setText(R.string.is_typing)
        message.setTypeface(null, ITALIC)
        message.setTextColor(MethodChecker.getColor(message.context, R.color.Green_G500))
    }

    private fun bindMessageState(lastReplyMessage: String) {
        message.text = MethodChecker.fromHtml(lastReplyMessage)
        message.setTypeface(null, NORMAL)
        message.setTextColor(MethodChecker.getColor(message.context, R.color.Neutral_N700_68))
    }

    private fun bindReadState(readStatus: Int?, unreads: Int?) {
        when (readStatus) {
            STATE_CHAT_UNREAD -> {
                userName.setWeight(Typography.TypographyWeight.BOLD)
                unreadCounter.show()
                unreadCounter.text = unreads.toZeroIfNull().toString()
            }

            STATE_CHAT_READ -> {
                userName.setWeight(Typography.TypographyWeight.REGULAR)
                unreadCounter.hide()
            }
        }
    }
}