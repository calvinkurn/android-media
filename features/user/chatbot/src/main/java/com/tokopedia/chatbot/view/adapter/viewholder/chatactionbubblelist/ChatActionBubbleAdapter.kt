package com.tokopedia.chatbot.view.adapter.viewholder.chatactionbubblelist

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.data.chatactionbubble.ChatActionBubbleViewModel
import java.util.*

/**
 * Created by Hendri on 18/07/18.
 */
class ChatActionBubbleAdapter(private val listener: OnChatActionSelectedListener) : RecyclerView.Adapter<ChatActionBubbleViewHolder>() {
    private val data = ArrayList<ChatActionBubbleViewModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatActionBubbleViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_chat_action_bubble, parent, false)
        return ChatActionBubbleViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ChatActionBubbleViewHolder, position: Int) {
        holder.bind(data[position])
        holder.itemView.setOnClickListener { v: View -> listener.onChatActionSelected(data[position]) }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun clearDataList() {
        val size = data.size
        data.clear()
        notifyItemRangeRemoved(0, size)
    }

    fun setDataList(elements: List<ChatActionBubbleViewModel>) {
        data.clear()
        data.addAll(elements)
        notifyDataSetChanged()
    }

    interface OnChatActionSelectedListener {
        fun onChatActionSelected(selected: ChatActionBubbleViewModel)
    }
}
