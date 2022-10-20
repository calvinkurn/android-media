package com.tokopedia.chatbot.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.chatbot.R
import com.tokopedia.unifyprinciples.Typography

class MediaRetryBottomSheetAdapter(private val onBottomSheetItemClicked: (position: Int) -> Unit) : RecyclerView.Adapter<MediaRetryBottomSheetAdapter.MediaRetryBottomSheetViewHoder>() {

    val list = mutableListOf<String>()

    inner class MediaRetryBottomSheetViewHoder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val item: Typography = itemView.findViewById<Typography>(R.id.retry_image_upload_bottom_sheet_item)
        fun bind(item: String, position: Int) {
            this.item.text = item
            itemView.setOnClickListener { onBottomSheetItemClicked(position) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaRetryBottomSheetViewHoder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.retry_upload_media_bottom_sheet_item, parent, false)
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


