package com.tokopedia.chatbot.view.adapter.viewholder.chatactionbubblelist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.chatbot.data.chatactionbubble.ChatActionBubbleUiModel
import com.tokopedia.chatbot.databinding.ItemChatHelpfullBinding
import com.tokopedia.chatbot.databinding.ItemReadMoreActionBubbleBinding
import java.util.*

/**
 * Created by Hendri on 18/07/18.
 */
const val MORE_DETAILS_TEXT = "Selengkapnya"
private const val HIDE_TEXT = "Sembunyikan"

class ChatActionBubbleAdapter(private val listener: OnChatActionSelectedListener) : RecyclerView.Adapter<BaseChatActionBubbleViewHolder>() {
    private val data = ArrayList<ChatActionBubbleUiModel>()
    private val dataPool = ArrayList<ChatActionBubbleUiModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseChatActionBubbleViewHolder {
        val holder: BaseChatActionBubbleViewHolder?
        if (viewType == 0) {
            val itemView = ItemChatHelpfullBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            holder = ChatActionBubbleViewHolder(itemView.root)
        } else {
            val itemView = ItemReadMoreActionBubbleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            holder = ChatActionBubbleReadMoreViewHolder(itemView.root)
        }

        return holder
    }

    override fun onBindViewHolder(holder: BaseChatActionBubbleViewHolder, position: Int) {
        holder.bind(data[position], onSelect(position))
    }

    private fun onSelect(position: Int): (Int) -> Unit = {
        if (it == 0) {
            listener.onChatActionSelected(data[position])
        } else {
            setListOnButtonCLick()
        }
    }

    private fun setListOnButtonCLick() {
        if (data.last().text == MORE_DETAILS_TEXT) {
            data.clear()
            data.addAll(dataPool)
            data.add(ChatActionBubbleUiModel(HIDE_TEXT, bubbleType = 1))
        } else {
            data.clear()
            data.addAll(getFirstFive(dataPool))
            data.add(ChatActionBubbleUiModel(text = MORE_DETAILS_TEXT, bubbleType = 1))
        }
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun getItemViewType(position: Int): Int {
        return data[position].bubbleType
    }

    fun clearDataList() {
        val size = data.size
        data.clear()
        notifyItemRangeRemoved(0, size)
    }

    fun setDataList(elements: List<ChatActionBubbleUiModel>) {
        data.clear()
        dataPool.clear()
        dataPool.addAll(elements)
        if (elements.size > 5) {
            data.addAll(getFirstFive(elements))
            data.add(ChatActionBubbleUiModel(text = MORE_DETAILS_TEXT, bubbleType = 1))
        } else {
            data.addAll(elements)
        }
        notifyDataSetChanged()
    }

    private fun getFirstFive(elements: List<ChatActionBubbleUiModel>): Collection<ChatActionBubbleUiModel> {
        val list = mutableListOf<ChatActionBubbleUiModel>()
        list.addAll(elements.subList(0, 5))
        return list
    }

    interface OnChatActionSelectedListener {
        fun onChatActionSelected(selected: ChatActionBubbleUiModel)
    }
}
