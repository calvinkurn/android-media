package com.tokopedia.chatbot.chatbot2.view.adapter.viewholder.helpfullquestionoptionlist

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.chatbot.chatbot2.view.uimodel.chatactionbubble.ChatActionBubbleUiModel
import com.tokopedia.chatbot.chatbot2.view.uimodel.helpfullquestion.ChatOptionListUiModel
import com.tokopedia.chatbot.databinding.ItemChatbotHelpfulBinding
import java.util.*

class ChatOptionListAdapter(private val onOptionListSelected: (ChatOptionListUiModel) -> Unit) : RecyclerView.Adapter<ChatOptionListViewHolder>() {
    private val data = ArrayList<ChatOptionListUiModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatOptionListViewHolder {
        val itemView = ItemChatbotHelpfulBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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

    @SuppressLint("NotifyDataSetChanged")
    fun setDataList(elements: List<ChatOptionListUiModel>) {
        data.clear()
        data.addAll(elements)
        notifyDataSetChanged()
    }

    interface OnChatActionSelectedListener {
        fun onChatActionSelected(selected: ChatActionBubbleUiModel)
    }
}
