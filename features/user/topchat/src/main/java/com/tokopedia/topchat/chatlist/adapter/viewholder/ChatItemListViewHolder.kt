package com.tokopedia.topchat.chatlist.adapter.viewholder

import android.graphics.Typeface.*
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
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
import com.tokopedia.topchat.common.util.ImageUtil
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.time.TimeHelper

/**
 * @author : Steven 2019-08-07
 */
class ChatItemListViewHolder constructor(
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
    private val unreadSpanColor: Int = MethodChecker.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_G500)
    private val readSpanColor: Int = MethodChecker.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_N700_68)
    private val typingImage: ImageUnify = itemView.findViewById(com.tokopedia.chat_common.R.id.iv_typing)
    private val typingText: Typography = itemView.findViewById(com.tokopedia.chat_common.R.id.tv_typing)

    private val menu = LongClickMenu()

    override fun bind(element: ItemChatListPojo, payloads: MutableList<Any>) {
        super.bind(element, payloads)
        when (getFirstPayload(payloads)) {
            PAYLOAD_READ_STATE -> bindReadState(element)
            PAYLOAD_TYPING_STATE -> bindTypingState()
            PAYLOAD_STOP_TYPING_STATE -> bindMessageState(element)
            PAYLOAD_UPDATE_PIN_STATUS -> bindPin(element)
            PAYLOAD_NEW_INCOMING_CHAT -> bindNewIncomingChat(element)
            else -> bind(element)
        }
    }

    private fun bindNewIncomingChat(element: ItemChatListPojo) {
        bindTimeStamp(element)
        bindMessageState(element)
        bindSmartReplyIndicator(element)
    }

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
        ImageUtil.setTypingAnimation(typingImage)
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
            if (chat.hasLabel()) {
                bindMessageState(chat)
            }
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
            when (itemMenus.icon) {
                R.drawable.ic_topchat_unpin_chat -> unpinChat(element)
                R.drawable.ic_topchat_pin_chat -> pinChat(element)
            }
        }
    }

    private fun unpinChat(element: ItemChatListPojo) {
        listener.pinUnpinChat(element, adapterPosition, false)
    }

    private fun pinChat(element: ItemChatListPojo) {
        listener.pinUnpinChat(element, adapterPosition, true)
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

            var pinText: String = ""
            @DrawableRes val pinDrawable: Int

            if (element.isPinned) {
                pinText = getString(R.string.menu_unpin_chat)
                pinDrawable = R.drawable.ic_topchat_unpin_chat
            } else {
                pinText = getString(R.string.menu_pin_chat)
                pinDrawable = R.drawable.ic_topchat_pin_chat
            }
            menus.add(Menus.ItemMenus(pinText, pinDrawable))

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
        typingImage.show()
        message.hide()
        typingText.show()
        ImageUtil.startAVDTypingAnimation(typingImage)
    }

    private fun bindMessageState(chat: ItemChatListPojo) {
        hideTyping()
        val spanText = SpannableStringBuilder()
        val lastMsg = MethodChecker.fromHtml(chat.lastReplyMessage)
        if (chat.label.isNotEmpty()) {
            val labelSpan = createLabelSpan(chat)
            spanText.append(labelSpan)
        }
        spanText.append(lastMsg)
        message.text = spanText
        message.setLines(2)
        message.maxLines = 2
        message.setTypeface(null, NORMAL)
        message.setTextColor(MethodChecker.getColor(message.context, com.tokopedia.unifyprinciples.R.color.Unify_N700_68))
    }

    private fun hideTyping() {
        message.show()
        typingImage.hide()
        typingText.hide()
        ImageUtil.stopAVDTypingAnimation(typingImage)
    }

    private fun createLabelSpan(chat: ItemChatListPojo): SpannableString {
        val labelSpan = SpannableString("${chat.label} • ")
        val color = if (chat.isUnread()) {
            unreadSpanColor
        } else {
            readSpanColor
        }
        labelSpan.setSpan(
                ForegroundColorSpan(color),
                0,
                labelSpan.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        labelSpan.setSpan(
                StyleSpan(BOLD),
                0,
                labelSpan.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return labelSpan
    }

    private fun bindReadState(chatItem: ItemChatListPojo) {
        when (chatItem.attributes?.readStatus) {
            STATE_CHAT_UNREAD -> {
                userName.setWeight(Typography.BOLD)
                unreadCounter.text = chatItem.totalUnread
                unreadCounter.show()
            }
            STATE_CHAT_READ -> {
                unreadCounter.hide()
            }
        }
    }

    private fun bindTimeStamp(chat: ItemChatListPojo) {
        time.text = TimeHelper.getRelativeTimeFromNow(chat.lastReplyTimeMillis)
    }

    private fun bindLabel(chat: ItemChatListPojo) {
        when (chat.tag) {
            OFFICIAL_TAG -> {
                label.text = chat.tag
                label.setLabelType(Label.GENERAL_LIGHT_BLUE)
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
        const val PAYLOAD_UPDATE_PIN_STATUS = 5432
        const val PAYLOAD_NEW_INCOMING_CHAT = 5433

        const val BUYER_TAG = "Pengguna"
        const val SELLER_TAG = "Penjual"
        const val OFFICIAL_TAG = "Official"
    }

}