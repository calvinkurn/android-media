package com.tokopedia.chat_common.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.chat_common.domain.pojo.ChatMenu
import com.tokopedia.chat_common.view.adapter.viewholder.chatmenu.BaseChatMenuViewHolder
import com.tokopedia.chat_common.view.adapter.viewholder.factory.ChatMenuFactory

class ChatMenuAdapter(
        private val menuItems: List<ChatMenu>,
        private val chatMenuListener: BaseChatMenuViewHolder.ChatMenuListener
) : RecyclerView.Adapter<BaseChatMenuViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseChatMenuViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(BaseChatMenuViewHolder.LAYOUT, parent, false)
        return ChatMenuFactory.create(chatMenuListener, view, viewType)
    }

    override fun getItemCount(): Int {
        return menuItems.size
    }

    override fun onBindViewHolder(holder: BaseChatMenuViewHolder, position: Int) {
        holder.bind(menuItems[position])
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}