package com.tokopedia.chatbot.chatbot2.view.bottomsheet.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.chatbot.chatbot2.data.rejectreasons.DynamicAttachmentRejectReasons
import com.tokopedia.chatbot.chatbot2.view.bottomsheet.listener.ChatbotRejectReasonsChipListener
import com.tokopedia.chatbot.databinding.ItemChatbotRejectReasonsBinding
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.unifycomponents.ChipsUnify.Companion.TYPE_NORMAL
import com.tokopedia.unifycomponents.ChipsUnify.Companion.TYPE_SELECTED

class ChatbotRejectReasonsAdapter(
    private val rejectReasonsChipListener: ChatbotRejectReasonsChipListener?
) : RecyclerView.Adapter<ChatbotRejectReasonsAdapter.ChatbotRejectReasonsViewHolder>() {

    val list = mutableListOf<DynamicAttachmentRejectReasons.RejectReasonFeedbackForm.RejectReasonReasonChip>()
    var selectedList = mutableListOf<DynamicAttachmentRejectReasons.RejectReasonFeedbackForm.RejectReasonReasonChip>()
    var chipListener: ChatbotRejectReasonsChipListener? = null

    inner class ChatbotRejectReasonsViewHolder(itemView: ItemChatbotRejectReasonsBinding) : RecyclerView.ViewHolder(itemView.root) {
        var item: ChipsUnify = itemView.chip
        fun bind(item: DynamicAttachmentRejectReasons.RejectReasonFeedbackForm.RejectReasonReasonChip, position: Int) {
            this.item.chipText = item.text
            selectedList.forEach {
                if (it.code == item.code) {
                    this.item.chipType = TYPE_SELECTED
                }
            }
            this.item.setOnClickListener {
                if (selectedList.contains(item)) {
                    selectedList.remove(item)
                    this.item.chipType = TYPE_NORMAL
                } else {
                    selectedList.add(item)
                    this.item.chipType = TYPE_SELECTED
                }
                rejectReasonsChipListener?.onChipClick(selectedList.size)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatbotRejectReasonsViewHolder {
        val view = ItemChatbotRejectReasonsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChatbotRejectReasonsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ChatbotRejectReasonsViewHolder, position: Int) {
        holder.bind(list[position], position)
    }

    fun setListener(listener: ChatbotRejectReasonsChipListener) {
        this.chipListener = listener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(
        list: List<DynamicAttachmentRejectReasons.RejectReasonFeedbackForm.RejectReasonReasonChip>,
        selectedReasonList: MutableList<DynamicAttachmentRejectReasons.RejectReasonFeedbackForm.RejectReasonReasonChip>
    ) {
        this.list.clear()
        this.list.addAll(list)
        selectedList = selectedReasonList
        notifyDataSetChanged()
    }
}
