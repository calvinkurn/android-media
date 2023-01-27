package com.tokopedia.chatbot.view.bottomsheet.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.chat_common.data.MessageUiModel
import com.tokopedia.chatbot.databinding.ItemChatbotReplyButtonBinding
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.unifyprinciples.Typography

class ReplyBubbleBottomSheetAdapter(
    private val messageUiModel: MessageUiModel
) :
    RecyclerView.Adapter<ReplyBubbleBottomSheetAdapter.ReplyBubbleBottomSheetViewHolder>() {

    val list = mutableListOf<Pair<String, Int>>()
    private var listener: ReplyBubbleBottomSheetListener? = null

    fun setListener(listener: ReplyBubbleBottomSheetListener) {
        this.listener = listener
    }

    inner class ReplyBubbleBottomSheetViewHolder(itemView: ItemChatbotReplyButtonBinding) : RecyclerView.ViewHolder(itemView.root) {
        private val title: Typography = itemView.title
        private val icon: IconUnify = itemView.icon
        fun bind(item: Pair<String, Int>) {
            title.text = item.first
            icon.setImage(item.second)
            itemView.setOnClickListener { listener?.onClickMessageReply(messageUiModel) }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ReplyBubbleBottomSheetViewHolder {
        val view = ItemChatbotReplyButtonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReplyBubbleBottomSheetViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReplyBubbleBottomSheetViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setList(list: List<Pair<String, Int>>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    interface ReplyBubbleBottomSheetListener {
        fun onClickMessageReply(messageUiModel: MessageUiModel)
    }
}
