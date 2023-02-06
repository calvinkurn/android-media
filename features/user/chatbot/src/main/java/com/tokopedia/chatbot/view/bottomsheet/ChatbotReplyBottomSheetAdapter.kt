package com.tokopedia.chatbot.view.bottomsheet

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.chat_common.data.MessageUiModel
import com.tokopedia.chatbot.databinding.ReplyButtonItemviewBinding
import com.tokopedia.chatbot.view.uimodel.ChatbotReplyOptionsUiModel
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.unifyprinciples.Typography

class ChatbotReplyBottomSheetAdapter(
    private val callback: (ChatbotReplyOptionsUiModel) -> Unit
) :
    RecyclerView.Adapter<ChatbotReplyBottomSheetAdapter.ReplyBubbleBottomSheetViewHolder>() {

    val list = mutableListOf<ChatbotReplyOptionsUiModel>()
    private var listener: ReplyBubbleBottomSheetListener? = null

    inner class ReplyBubbleBottomSheetViewHolder(itemView: ReplyButtonItemviewBinding) : RecyclerView.ViewHolder(itemView.root) {
        private val title: Typography = itemView.title
        private val icon: IconUnify = itemView.icon
        fun bind(item: ChatbotReplyOptionsUiModel) {
            title.text = item.title
            icon.setImage(item.icon)
            itemView.setOnClickListener {
                callback(item)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ReplyBubbleBottomSheetViewHolder {
        val view = ReplyButtonItemviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReplyBubbleBottomSheetViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReplyBubbleBottomSheetViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: List<ChatbotReplyOptionsUiModel>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    interface ReplyBubbleBottomSheetListener {
        fun onClickMessageReply(messageUiModel: MessageUiModel)
    }
}
