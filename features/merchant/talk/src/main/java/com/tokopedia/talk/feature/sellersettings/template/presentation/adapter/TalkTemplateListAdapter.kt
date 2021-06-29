package com.tokopedia.talk.feature.sellersettings.template.presentation.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.talk.R
import com.tokopedia.talk.feature.sellersettings.template.presentation.fragment.TalkTemplateListFragment
import com.tokopedia.talk.feature.sellersettings.template.presentation.listener.TalkTemplateListListener
import com.tokopedia.talk.feature.sellersettings.template.presentation.util.ItemTouchHelperAdapter

class TalkTemplateListAdapter(private val talkTemplateListListener: TalkTemplateListListener) :
        RecyclerView.Adapter<TalkTemplateListViewHolder>(), ItemTouchHelperAdapter {

    private val templates = mutableListOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TalkTemplateListViewHolder {
        return TalkTemplateListViewHolder(parent.inflateLayout(R.layout.item_talk_template, false), talkTemplateListListener)
    }

    override fun getItemCount(): Int {
        return templates.size
    }

    override fun onBindViewHolder(holder: TalkTemplateListViewHolder, position: Int) {
        holder.bind(templates[position])
    }

    override fun onItemDismiss(position: Int) {
        // No Op
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
        notifyDataSetChanged()
    }

    fun shouldShowAddTemplateButton(): Boolean {
        return templates.size < TalkTemplateListFragment.MAX_TEMPLATE_SIZE
    }

}