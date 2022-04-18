package com.tokopedia.chatbot.view.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.chatbot.R
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.unifyprinciples.Typography

class ReplyBubbleBottomSheetAdapter(private val onReplyBottomSheetItemClicked: (position: Int) -> Unit) :
    RecyclerView.Adapter<ReplyBubbleBottomSheetAdapter.ReplyBubbleBottomSheetViewHolder>() {

    val list = mutableListOf<Pair<String,Int>>()

    init {
        list.add(Pair("Balas",IconUnify.REPLY))
    }

    inner class ReplyBubbleBottomSheetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: Typography = itemView.findViewById(R.id.title)
        val icon: IconUnify = itemView.findViewById(R.id.icon)
        fun bind(item: Pair<String, Int>, position: Int) {
            title.text = item.first
            //TODO review this
            icon.setImage(item.second)
            itemView.setOnClickListener { onReplyBottomSheetItemClicked(position) }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ReplyBubbleBottomSheetViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.reply_button_itemview, parent, false)
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