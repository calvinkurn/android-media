package com.tokopedia.topchat.chatroom.view.adapter.viewholder.chatmenu

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.domain.pojo.ChatMenu

abstract class BaseChatMenuViewHolder(private val listener: ChatMenuListener, itemView: View?) : RecyclerView.ViewHolder(itemView) {

    private val menuIcon: ImageView? = itemView?.findViewById(R.id.ivIcon)
    private val menuTitle: TextView? = itemView?.findViewById(R.id.tvTitle)

    interface ChatMenuListener {

    }

    fun bind(chatMenu: ChatMenu) {
        menuIcon?.setImageResource(chatMenu.icon)
        menuTitle?.text = chatMenu.title

        itemView.setOnClickListener {
            onItemClick()
        }
    }

    abstract fun onItemClick()

    companion object {
        val LAYOUT = R.layout.item_chat_menu
    }
}