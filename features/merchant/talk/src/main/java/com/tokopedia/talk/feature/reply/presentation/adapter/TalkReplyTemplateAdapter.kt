package com.tokopedia.talk.feature.reply.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.talk.R
import com.tokopedia.talk.feature.reply.presentation.adapter.viewholder.TalkReplyTemplateViewHolder
import com.tokopedia.talk.feature.reply.presentation.widget.listeners.TalkReplyTemplateListener

class TalkReplyTemplateAdapter(private val talkReplyTemplateListener: TalkReplyTemplateListener) : RecyclerView.Adapter<TalkReplyTemplateViewHolder>() {

    private val templates: MutableList<String> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TalkReplyTemplateViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_talk_reply_template, parent, false)
        return TalkReplyTemplateViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return templates.size
    }

    override fun onBindViewHolder(holder: TalkReplyTemplateViewHolder, position: Int) {
        holder.bind(templates[position], talkReplyTemplateListener)
    }

    fun setData(templates: List<String>) {
        this.templates.apply {
            clear()
            addAll(templates)
        }
        notifyDataSetChanged()
    }
}