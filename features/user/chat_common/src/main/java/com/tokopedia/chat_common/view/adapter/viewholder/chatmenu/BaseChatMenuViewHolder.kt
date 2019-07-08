package com.tokopedia.chat_common.view.adapter.viewholder.chatmenu

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.chat_common.R
import com.tokopedia.chat_common.domain.pojo.ChatMenu

abstract class BaseChatMenuViewHolder(val listener: ChatMenuListener, itemView: View?) : RecyclerView.ViewHolder(itemView) {

    private val menuIcon: ImageView? = itemView?.findViewById(R.id.ivIcon)
    private val menuTitle: TextView? = itemView?.findViewById(R.id.tvTitle)

    interface ChatMenuListener {
        fun showChatMenu()
        fun closeChatMenu()
        fun onClickAttachProduct()
        fun onClickImagePicker()
    }

    fun bind(chatMenu: ChatMenu) {
        menuIcon?.setImageResource(chatMenu.icon)
        menuTitle?.text = chatMenu.title

        itemView.setOnClickListener {
            listener.closeChatMenu()
            onItemClick()
        }
    }

    abstract fun onItemClick()

    companion object {
        val LAYOUT = R.layout.item_chat_menu
    }
}