package com.tokopedia.play.ui.chatlist.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.play.R
import com.tokopedia.play.ui.chatlist.model.PlayChat
import com.tokopedia.play.ui.chatlist.viewholder.ChatViewHolder

/**
 * Created by jegul on 04/12/19
 */
class ChatAdapter(
        initialList: List<PlayChat> = emptyList()
) : RecyclerView.Adapter<ChatViewHolder>() {

    private val itemList = mutableListOf<PlayChat>()

    init {
        itemList.addAll(initialList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        return ChatViewHolder(
                View.inflate(parent.context, R.layout.item_play_chat, null)
        )
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    fun addChat(chat: PlayChat) {
        itemList.add(chat)
        notifyItemInserted(itemCount)
    }
}