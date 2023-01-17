package com.tokopedia.chatbot.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.databinding.ReplyButtonItemviewBinding
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.unifyprinciples.Typography

class ReplyBubbleBottomSheetAdapter(private val onReplyBottomSheetItemClicked: (position: Int) -> Unit) :
    RecyclerView.Adapter<ReplyBubbleBottomSheetAdapter.ReplyBubbleBottomSheetViewHolder>() {

    val list = mutableListOf<Pair<String,Int>>()

    init {
        list.add(Pair("Balas",IconUnify.REPLY))
    }

    inner class ReplyBubbleBottomSheetViewHolder(itemView: ReplyButtonItemviewBinding) : RecyclerView.ViewHolder(itemView.root) {
        private val title: Typography = itemView.title
        private val icon: IconUnify = itemView.icon
        fun bind(item: Pair<String, Int>, position: Int) {
            title.text = item.first
            icon.setImage(item.second)
            itemView.setOnClickListener { onReplyBottomSheetItemClicked(position) }
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
        holder.bind(list[position], position)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setList(list: List<Pair<String,Int>>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }
}