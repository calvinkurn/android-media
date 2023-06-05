package com.tokopedia.chatbot.view.bottomsheet.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.chatbot.databinding.ItemChatbotRetryUploadMediaBinding
import com.tokopedia.unifyprinciples.Typography

class MediaRetryBottomSheetAdapter(private val onBottomSheetItemClicked: (position: Int) -> Unit) : RecyclerView.Adapter<MediaRetryBottomSheetAdapter.MediaRetryBottomSheetViewHoder>() {

    val list = mutableListOf<String>()

    inner class MediaRetryBottomSheetViewHoder(itemView: ItemChatbotRetryUploadMediaBinding) : RecyclerView.ViewHolder(itemView.root) {
        val item: Typography = itemView.retryImageUploadBottomSheetItem
        fun bind(item: String, position: Int) {
            this.item.text = item
            itemView.setOnClickListener { onBottomSheetItemClicked(position) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaRetryBottomSheetViewHoder {
        val view = ItemChatbotRetryUploadMediaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MediaRetryBottomSheetViewHoder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MediaRetryBottomSheetViewHoder, position: Int) {
        holder.bind(list[position], position)
    }

    fun setList(list: List<String>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }
}
