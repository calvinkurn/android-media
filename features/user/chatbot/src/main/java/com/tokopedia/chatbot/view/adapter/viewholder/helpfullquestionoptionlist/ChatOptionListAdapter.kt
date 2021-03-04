package com.tokopedia.chatbot.view.adapter.viewholder.helpfullquestionoptionlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.data.chatactionbubble.ChatActionBubbleViewModel
import com.tokopedia.chatbot.data.helpfullquestion.ChatOptionListViewModel
import com.tokopedia.chatbot.domain.pojo.helpfullquestion.HelpFullQuestionPojo
import java.util.*


class ChatOptionListAdapter(private val onOptionListSelected: (ChatOptionListViewModel) -> Unit) : RecyclerView.Adapter<ChatOptionListViewHolder>() {
    private val data = ArrayList<ChatOptionListViewModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatOptionListViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_chat_helpfull, parent, false)
        return ChatOptionListViewHolder(itemView, onOptionListSelected)
    }

    override fun onBindViewHolder(holder: ChatOptionListViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun clearDataList() {
        val size = data.size
        data.clear()
        notifyItemRangeRemoved(0, size)
    }

    fun setDataList(elements: List<ChatOptionListViewModel>) {
        data.clear()
        data.addAll(elements)
        notifyDataSetChanged()
    }

    interface OnChatActionSelectedListener {
        fun onChatActionSelected(selected: ChatActionBubbleViewModel)
    }
}
