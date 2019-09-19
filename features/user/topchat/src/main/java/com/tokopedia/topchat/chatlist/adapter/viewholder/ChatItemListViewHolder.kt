package com.tokopedia.topchat.chatlist.adapter.viewholder

import android.graphics.Typeface.ITALIC
import android.graphics.Typeface.NORMAL
import android.support.annotation.LayoutRes
import android.text.format.DateFormat
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatlist.listener.ChatListItemListener
import com.tokopedia.topchat.chatlist.pojo.ItemChatListPojo
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifyprinciples.Typography
import java.util.*

/**
 * @author : Steven 2019-08-07
 */
class ChatItemListViewHolder(
        itemView: View,
        var listener: ChatListItemListener
) : AbstractViewHolder<ItemChatListPojo>(itemView) {

    private val userName: Typography = itemView.findViewById(R.id.user_name)
    private val thumbnail: ImageView = itemView.findViewById(R.id.thumbnail)
    private val message: TextView = itemView.findViewById(R.id.message)
    private val unreadCounter: Typography = itemView.findViewById(R.id.unread_counter)
    private val time: Typography = itemView.findViewById(R.id.time)
    private val label: Label = itemView.findViewById(R.id.user_label)

    override fun bind(element: ItemChatListPojo) {
        val attributes = element.attributes
        val data = attributes?.contact

        data?.let { contact ->
            itemView.setOnClickListener {
                attributes.readStatus = STATE_CHAT_READ
                bindReadState(attributes.readStatus, attributes.unreads)
                listener.chatItemClicked(element)
            }

            itemView.setOnLongClickListener {
                listener.chatItemDeleted(element, adapterPosition)
                true
            }

            userName.text = contact.contactName
            ImageHandler.loadImageCircle2(itemView.context, thumbnail, contact.thumbnail)

            bindReadState(attributes.readStatus, attributes.unreads)
            bindMessageState(attributes.lastReplyMessage)
            bindTimeStamp(attributes.lastReplyTimeStr)
            bindLabel(contact.tag)
        }

    }

    override fun bind(element: ItemChatListPojo, payloads: MutableList<Any>) {
        super.bind(element, payloads)
        if (payloads.isEmpty() || payloads.first() !is Int) return

        when (payloads.first() as Int) {
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
                userName.setWeight(Typography.BOLD)
                unreadCounter.show()
            }

            STATE_CHAT_READ -> {
                userName.setWeight(Typography.REGULAR)
                unreadCounter.hide()
            }
        }
    }

    private fun bindTimeStamp(lastReplyTimeStr: String) {
        time.text = convertToRelativeDate(lastReplyTimeStr)
    }

    private fun bindLabel(tag: String) {
        when (tag) {
            OFFICIAL_TAG -> {
                label.text = tag
                label.setLabelType(Label.GENERAL_LIGHT_BLUE)
                label.show()
            }
            SELLER_TAG -> {
                label.text = tag
                label.setLabelType(Label.GENERAL_LIGHT_GREEN)
                label.show()
            }
            else -> label.hide()
        }

    }

    private fun convertToRelativeDate(timeStamp: String): String {
        val smsTime = Calendar.getInstance()
        smsTime.timeInMillis = timeStamp.toLongOrZero()

        val now = Calendar.getInstance()

        val timeFormatString = "hh:mm"
        val dateTimeFormatString = "dd MMM"
        val dateTimeYearFormatString = "dd MMM yy"
        val HOURS = (60 * 60 * 60).toLong()
        return if ((now.get(Calendar.DATE) == smsTime.get(Calendar.DATE))
                && (now.get(Calendar.MONTH) == smsTime.get(Calendar.MONTH))) {
            DateFormat.format(timeFormatString, smsTime).toString()
        } else if ((now.get(Calendar.DATE) - smsTime.get(Calendar.DATE) == 1)
                && (now.get(Calendar.MONTH) == smsTime.get(Calendar.MONTH))){
            "Kemarin"
        } else if (now.get(Calendar.YEAR) == smsTime.get(Calendar.YEAR)) {
            DateFormat.format(dateTimeFormatString, smsTime).toString()
        } else {
            DateFormat.format(dateTimeYearFormatString, smsTime).toString()
        }
    }

    companion object {
        @LayoutRes val LAYOUT = R.layout.item_chat_list

        //state of chat
        const val STATE_CHAT_UNREAD = 1
        const val STATE_CHAT_READ = 2

        //message payload
        const val PAYLOAD_READ_STATE = 8796
        const val PAYLOAD_TYPING_STATE = 3207
        const val PAYLOAD_STOP_TYPING_STATE = 5431

        const val SELLER_TAG = "Penjual"
        const val OFFICIAL_TAG = "Official"
    }

}