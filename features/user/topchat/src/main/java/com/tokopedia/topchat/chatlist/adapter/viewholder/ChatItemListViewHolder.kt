package com.tokopedia.topchat.chatlist.adapter.viewholder

import android.graphics.Typeface.ITALIC
import android.graphics.Typeface.NORMAL
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.design.component.Menus
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatlist.listener.ChatListItemListener
import com.tokopedia.topchat.chatlist.pojo.ChatStateItem
import com.tokopedia.topchat.chatlist.pojo.ItemChatListPojo
import com.tokopedia.topchat.chatlist.widget.LongClickMenu
import com.tokopedia.topchat.common.util.ChatHelper
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success

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
    private val pin: ImageView = itemView.findViewById(R.id.ivPin)
    private val smartReplyIndicator: View? = itemView.findViewById(R.id.view_smart_reply_indicator)

    private val menu = LongClickMenu()

    override fun bind(element: ItemChatListPojo) {
        bindItemChatClick(element)
        bindItemChatLongClick(element)
        bindName(element)
        bindProfilePicture(element)
        bindMessageState(element)
        bindTimeStamp(element)
        bindLabel(element)
        bindPin(element)
        bindSmartReplyIndicator(element)
    }

    private fun bindSmartReplyIndicator(element: ItemChatListPojo) {
        if (element.isReplyTopBot() && element.isUnread() && listener.isTabSeller()) {
            smartReplyIndicator?.show()
            unreadCounter.hide()
        } else {
            smartReplyIndicator?.hide()
            bindReadState(element)
        }
    }

    override fun bind(element: ItemChatListPojo, payloads: MutableList<Any>) {
        super.bind(element, payloads)
        when (getFirstPayload(payloads)) {
            PAYLOAD_READ_STATE -> bindReadState(element)
            PAYLOAD_TYPING_STATE -> bindTypingState()
            PAYLOAD_STOP_TYPING_STATE -> bindMessageState(element)
            else -> bind(element)
        }
    }

    private fun getFirstPayload(payloads: MutableList<Any>): Int? {
        if (payloads.isNotEmpty() && payloads.first() is Int) return payloads.first() as Int
        return null
    }

    private fun bindItemChatClick(element: ItemChatListPojo) {
        itemView.setOnClickListener {
            onChatItemClicked(element)
        }
    }

    private fun bindItemChatLongClick(element: ItemChatListPojo) {
        itemView.setOnLongClickListener {
            showLongClickMenu(element)
            true
        }
    }

    private fun bindName(chat: ItemChatListPojo) {
        userName.text = MethodChecker.fromHtml(chat.name)
        userName.setWeight(Typography.REGULAR)
    }

    private fun bindProfilePicture(chat: ItemChatListPojo) {
        ImageHandler.loadImageCircle2(itemView.context, thumbnail, chat.thumbnail)
    }

    private fun bindPin(chat: ItemChatListPojo) {
        pin.showWithCondition(chat.isPinned)
    }

    private fun onChatItemClicked(chat: ItemChatListPojo) {
        val attributes = chat.attributes

        if (chat.isUnread() && attributes != null) {
            chat.markAsRead()
            listener.decreaseNotificationCounter()
            bindSmartReplyIndicator(chat)
        }

        listener.chatItemClicked(chat, adapterPosition)
    }

    private fun showLongClickMenu(element: ItemChatListPojo) {
        if (menu.isAdded) return
        menu.apply {
            setItemMenuList(createChatLongClickMenu(element))
            setOnItemMenuClickListener { itemMenus, _ ->
                handleChatMenuClick(itemMenus, element)
                dismiss()
            }
        }.show(listener.getSupportChildFragmentManager(), LongClickMenu.TAG)
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
        listener.trackDeleteChat(element)
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
            if (element.hasTheSameMsgId(state) && state.isSuccess()) {
                changeStateMarkAsRead(element)
            }
        }
    }

    private fun changeStateMarkAsRead(element: ItemChatListPojo) {
        element.attributes?.let {
            with(it) {
                readStatus = STATE_CHAT_READ
                bindReadState(element)
                listener.decreaseNotificationCounter()
                listener.trackChangeReadStatus(element)
            }
        }
    }

    private fun responseSuccessChangeStateUnread(list: List<ChatStateItem>, element: ItemChatListPojo) {
        for (state in list) {
            if (element.hasTheSameMsgId(state) && state.isSuccess()) {
                changeStateMarkAsUnread(element)
            }
        }
    }

    private fun changeStateMarkAsUnread(element: ItemChatListPojo) {
        element.attributes?.let {
            with(it) {
                readStatus = STATE_CHAT_UNREAD
                unreadReply = 1
                bindReadState(element)
                listener.increaseNotificationCounter()
                listener.trackChangeReadStatus(element)
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

    private fun bindTypingState() {
        message.setText(R.string.is_typing)
        message.setTypeface(null, ITALIC)
        message.setTextColor(MethodChecker.getColor(message.context, com.tokopedia.unifyprinciples.R.color.Green_G500))
    }

    private fun bindMessageState(chat: ItemChatListPojo) {
        message.text = MethodChecker.fromHtml(chat.lastReplyMessage)
        message.setTypeface(null, NORMAL)
        message.setTextColor(MethodChecker.getColor(message.context, com.tokopedia.unifyprinciples.R.color.Neutral_N700_68))
    }

    private fun bindReadState(chatItem: ItemChatListPojo) {
        when (chatItem.attributes?.readStatus) {
            STATE_CHAT_UNREAD -> {
                userName.setWeight(Typography.BOLD)
                unreadCounter.text = chatItem.totalUnread
                unreadCounter.show()
            }
            STATE_CHAT_READ -> {
                userName.setWeight(Typography.REGULAR)
                unreadCounter.hide()
            }
        }
    }

    private fun bindTimeStamp(chat: ItemChatListPojo) {
        time.text = ChatHelper.convertToRelativeDate(chat.lastReplyTimeStr)
    }

    private fun bindLabel(chat: ItemChatListPojo) {
        when (chat.tag) {
            OFFICIAL_TAG -> {
                label.text = chat.tag
                label.setLabelType(Label.GENERAL_LIGHT_BLUE)
                label.show()
            }
            SELLER_TAG -> {
                label.text = chat.tag
                label.setLabelType(Label.GENERAL_LIGHT_GREEN)
                label.show()
            }
            else -> label.hide()
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

        const val BUYER_TAG = "Pengguna"
        const val SELLER_TAG = "Penjual"
        const val OFFICIAL_TAG = "Official"
    }

}