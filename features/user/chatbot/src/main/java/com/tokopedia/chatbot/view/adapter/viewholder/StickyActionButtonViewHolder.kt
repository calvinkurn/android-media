package com.tokopedia.chatbot.view.adapter.viewholder

import android.view.View
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.tokopedia.chat_common.view.adapter.viewholder.BaseChatViewHolder
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.data.stickyactionbutton.StickyActionButtonViewModel
import com.tokopedia.chatbot.util.ChatBotTimeConverter
import com.tokopedia.chatbot.view.adapter.viewholder.listener.StickyActionButtonClickListener
import com.tokopedia.unifycomponents.UnifyButton

class StickyActionButtonViewHolder(itemView: View,
                                   private val actionButtonClickListener: StickyActionButtonClickListener) : BaseChatViewHolder<StickyActionButtonViewModel>(itemView) {

    private val message: TextView = itemView.findViewById<TextView>(R.id.message)
    private val actionButton: TextView = itemView.findViewById<UnifyButton>(R.id.actionButton)

    override fun bind(element: StickyActionButtonViewModel) {
        super.bind(element)
        message.text = element.message
        actionButton.text = element.stickyActionButton?.firstOrNull()?.text
        actionButton.setOnClickListener {
            val stickyActionButton = element.stickyActionButton?.firstOrNull()
            stickyActionButton?.invoiceRefNum?.let { invoiceRefNum ->
                actionButtonClickListener.onStickyActionButtonClicked(invoiceRefNum, stickyActionButton.replyText
                        ?: "")
            }
        }
    }

    override fun getHourId(): Int {
        return R.id.hour
    }

    override fun getDateId(): Int {
        return R.id.date
    }

    override fun getHourTime(replyTime: String): String {
        return ChatBotTimeConverter.getHourTime(replyTime)
    }

    override fun alwaysShowTime(): Boolean = true

    companion object {

        @LayoutRes
        val LAYOUT = R.layout.sticky_action_buton_chat_layout
    }
}