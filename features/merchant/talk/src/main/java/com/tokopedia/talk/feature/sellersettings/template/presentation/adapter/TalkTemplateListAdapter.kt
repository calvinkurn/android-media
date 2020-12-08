package com.tokopedia.talk.feature.sellersettings.template.presentation.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.design.touchhelper.ItemTouchHelperAdapter
import com.tokopedia.talk.feature.sellersettings.template.presentation.listener.TalkTemplateListListener

class TalkTemplateListAdapter(private val talkTemplateListListener: TalkTemplateListListener) :
        RecyclerView.Adapter<TalkTemplateListViewHolder>(), ItemTouchHelperAdapter {

    private val templates = mutableListOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TalkTemplateListViewHolder {
        return TalkTemplateListViewHolder(parent, talkTemplateListListener)
    }

    override fun getItemCount(): Int {
        return templates.size
    }

    override fun onBindViewHolder(holder: TalkTemplateListViewHolder, position: Int) {
        holder.bind(templates[position])
    }

    override fun onItemDismiss(position: Int) {
        templates.removeAt(position)
        talkTemplateListListener.onItemRemoved(position)
        notifyItemRemoved(position)
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        val modelFrom = templates[fromPosition]
        templates.removeAt(fromPosition)
        templates.add(toPosition, modelFrom)
        notifyItemMoved(fromPosition, toPosition)
        talkTemplateListListener.onItemMove(fromPosition, toPosition)
        return true
    }

    fun setData(chatTemplates: List<String>) {
        templates.clear()
        templates.addAll(chatTemplates)
    }
}