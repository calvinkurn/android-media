package com.tokopedia.topchat.chatlist.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatlist.view.listener.ChatListTickerListener
import com.tokopedia.topchat.chatlist.view.uimodel.ChatListTickerUiModel
import com.tokopedia.topchat.databinding.ItemChatListTickerBinding
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.utils.view.binding.viewBinding

class ChatListTickerViewHolder(view: View,
                               private val chatListTickerListener: ChatListTickerListener
): AbstractViewHolder<ChatListTickerUiModel>(view) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_chat_list_ticker
    }

    private val binding: ItemChatListTickerBinding? by viewBinding()

    override fun bind(element: ChatListTickerUiModel) {
        binding?.chatListTicker?.run {
            tickerType = element.tickerType
            setHtmlDescription(element.message)
            setDescriptionClickEvent(object : TickerCallback {
                override fun onDescriptionViewClick(linkUrl: CharSequence) {
                    chatListTickerListener.onChatListTickerClicked()
                }

                override fun onDismiss() {
                    //no op
                }
            })
        }
    }
}
