package com.tokopedia.topchat.chatlist.adapter.viewholder

import android.graphics.Typeface.ITALIC
import android.graphics.Typeface.NORMAL
import androidx.annotation.LayoutRes
import com.google.android.material.snackbar.Snackbar
import android.text.format.DateFormat
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.design.component.Menus
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatlist.listener.ChatListItemListener
import com.tokopedia.topchat.chatlist.pojo.ItemChatListPojo
import com.tokopedia.topchat.chatlist.pojo.ChatStateItem
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
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
                onChatItemClicked(element)
            }

            itemView.setOnLongClickListener {
                showLongClickMenu(element)
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

    private fun onChatItemClicked(chat: ItemChatListPojo) {
        val attributes = chat.attributes

        if (chat.isUnread() && attributes != null) {
            chat.markAsRead()
            listener.decreaseNotificationCounter()
            bindReadState(attributes.readStatus, attributes.unreads)
        }

        listener.chatItemClicked(chat, adapterPosition)
    }

    private fun showLongClickMenu(element: ItemChatListPojo) {
        Menus(itemView.context, R.style.BottomFilterDialogTheme).apply {
            setTitle(" ")
            itemMenuList = createChatLongClickMenu(element)
            setOnItemMenuClickListener { itemMenus, _ ->
                handleChatMenuClick(itemMenus, element)
                dismiss()
            }
        }.show()
    }

    private fun handleChatMenuClick(itemMenus: Menus.ItemMenus, element: ItemChatListPojo) {
        with(itemView.context) {
            when (itemMenus.title) {
                getString(R.string.menu_delete_chat) -> delete(element)
                getString(R.string.menu_mark_as_read) -> markAsRead(element)
                getString(R.string.menu_mark_as_unread) -> markAsUnRead(element)
            }
        }
    }

    private fun delete(element: ItemChatListPojo) {
        listener.deleteChat(element, adapterPosition)
    }

    private fun markAsRead(element: ItemChatListPojo) {
        listener.markChatAsRead(element.ids()) { result ->
            when (result) {
                is Success -> responseSuccessChangeStateRead(result.data.chatState.list, element)
                is Fail -> failChangeChatState(result.throwable)
            }
        }
    }

    private fun markAsUnRead(element: ItemChatListPojo) {
        listener.markChatAsUnread(element.ids()) { result ->
            when (result) {
                is Success -> responseSuccessChangeStateUnread(result.data.chatState.list, element)
                is Fail -> failChangeChatState(result.throwable)
            }
        }
    }

    private fun failChangeChatState(throwable: Throwable) {
        val errorMessage = ErrorHandler.getErrorMessage(itemView.context, throwable)
        Toaster.showError(itemView, errorMessage, Snackbar.LENGTH_LONG)
    }

    private fun responseSuccessChangeStateRead(list: List<ChatStateItem>, element: ItemChatListPojo) {
        for (state in list) {
            if (element.msgId == state.msgID.toString() && state.isSuccess == 1) {
                changeStateMarkAsRead(element)
            }
        }
    }

    private fun changeStateMarkAsRead(element: ItemChatListPojo) {
        element.attributes?.let {
            with (it) {
                readStatus = STATE_CHAT_READ
                bindReadState(readStatus, unreads)
                listener.decreaseNotificationCounter()
            }
        }
    }

    private fun responseSuccessChangeStateUnread(list: List<ChatStateItem>, element: ItemChatListPojo) {
        for (state in list) {
            if (element.msgId == state.msgID.toString() && state.isSuccess == 1) {
                changeStateMarkAsUnread(element)
            }
        }
    }

    private fun changeStateMarkAsUnread(element: ItemChatListPojo) {
        element.attributes?.let {
            with (it) {
                readStatus = STATE_CHAT_UNREAD
                bindReadState(readStatus, unreads)
                listener.increaseNotificationCounter()
            }
        }
    }

    private fun createChatLongClickMenu(element: ItemChatListPojo): MutableList<Menus.ItemMenus> {
        with(itemView.context) {
            val menus = arrayListOf<Menus.ItemMenus>()
            val delete = getString(R.string.menu_delete_chat)
            val markAsRead = getString(R.string.menu_mark_as_read)
            val markAsUnread = getString(R.string.menu_mark_as_unread)

            if (element.hasUnreadItem()) {
                menus.add(Menus.ItemMenus(markAsRead, R.drawable.ic_chat_read_filled_grey))
            } else {
                menus.add(Menus.ItemMenus(markAsUnread, R.drawable.ic_chat_unread_filled_grey))
            }
            menus.add(Menus.ItemMenus(delete, R.drawable.ic_trash_filled_grey))

            return menus
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

        val timeFormatString = "HH:mm"
        val dateTimeFormatString = "dd MMM"
        val dateTimeYearFormatString = "dd MMM yy"
        val HOURS = (60 * 60 * 60).toLong()
        return if ((now.get(Calendar.DATE) == smsTime.get(Calendar.DATE))
                && (now.get(Calendar.MONTH) == smsTime.get(Calendar.MONTH))) {
            DateFormat.format(timeFormatString, smsTime).toString()
        } else if ((now.get(Calendar.DATE) - smsTime.get(Calendar.DATE) == 1)
                && (now.get(Calendar.MONTH) == smsTime.get(Calendar.MONTH))) {
            "Kemarin"
        } else if (now.get(Calendar.YEAR) == smsTime.get(Calendar.YEAR)) {
            DateFormat.format(dateTimeFormatString, smsTime).toString()
        } else {
            DateFormat.format(dateTimeYearFormatString, smsTime).toString()
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_chat_list

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