package com.tokopedia.chat_common.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.chat_common.domain.pojo.ChatMenu
import com.tokopedia.chat_common.view.adapter.viewholder.chatmenu.BaseChatMenuViewHolder
import com.tokopedia.chat_common.view.adapter.viewholder.factory.ChatMenuFactory

class ChatMenuAdapter(
        private val chatMenuFactory: ChatMenuFactory,
        private val chatMenuListener: BaseChatMenuViewHolder.ChatMenuListener
) : RecyclerView.Adapter<BaseChatMenuViewHolder>() {

    private val chatMenuItems = chatMenuFactory.createChatMenuItems()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseChatMenuViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(BaseChatMenuViewHolder.LAYOUT, parent, false)
        return chatMenuFactory.create(chatMenuListener, view, viewType)
    }

    override fun getItemCount(): Int {
        return chatMenuItems.size
    }

    override fun onBindViewHolder(holder: BaseChatMenuViewHolder, position: Int) {
        holder.bind(chatMenuItems[position])
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}