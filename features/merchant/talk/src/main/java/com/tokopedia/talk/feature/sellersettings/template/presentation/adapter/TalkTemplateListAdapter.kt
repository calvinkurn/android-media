package com.tokopedia.talk.feature.sellersettings.template.presentation.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.talk.feature.sellersettings.template.presentation.listener.TalkTemplateListListener

class TalkTemplateListAdapter(private val talkTemplateListListener: TalkTemplateListListener) : RecyclerView.Adapter<TalkTemplateListViewHolder>() {

    private val templates = listOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TalkTemplateListViewHolder {
        return TalkTemplateListViewHolder(parent, talkTemplateListListener)
    }

    override fun getItemCount(): Int {
        return templates.size
    }

    override fun onBindViewHolder(holder: TalkTemplateListViewHolder, position: Int) {
        holder.bind(templates[position])
    }
}