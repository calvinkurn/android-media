package com.tokopedia.chat_common.view.adapter.viewholder.chatmenu

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.chat_common.R
import com.tokopedia.chat_common.domain.pojo.ChatMenu
import com.tokopedia.chat_common.view.adapter.viewholder.factory.ChatMenuFactory

abstract class BaseChatMenuViewHolder(val listener: ChatMenuListener, itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val menuIcon: ImageView? = itemView.findViewById(R.id.ivIcon)
    private val menuTitle: TextView? = itemView.findViewById(R.id.tvTitle)

    interface ChatMenuListener {
        fun showChatMenu()
        fun closeChatMenu()
        fun onClickAttachProduct()
        fun onClickImagePicker()
        fun createChatMenuFactory(): ChatMenuFactory
        fun trackChatMenuClicked(label: String)
    }

    fun bind(chatMenu: ChatMenu) {
        menuIcon?.setImageResource(chatMenu.icon)
        menuTitle?.text = chatMenu.title

        itemView.setOnClickListener {
            onItemClick()
            listener.trackChatMenuClicked(chatMenu.label)
            listener.closeChatMenu()
        }
    }

    abstract fun onItemClick()

    companion object {
        val LAYOUT = R.layout.item_chat_menu
    }
}