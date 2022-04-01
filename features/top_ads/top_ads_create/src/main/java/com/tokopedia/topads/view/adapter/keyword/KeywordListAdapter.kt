package com.tokopedia.topads.view.adapter.keyword

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topads.common.data.response.KeywordDataItem
import com.tokopedia.topads.common.data.util.Utils
import com.tokopedia.topads.create.R
import com.tokopedia.topads.view.adapter.bidinfo.viewholder.HIGH
import com.tokopedia.topads.view.adapter.bidinfo.viewholder.LOW
import com.tokopedia.topads.view.adapter.bidinfo.viewholder.MEDIUM
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify
import com.tokopedia.unifyprinciples.Typography

/**
 * Author errysuprayogi on 14,November,2019
 */
class KeywordListAdapter(private val onChecked: ((position: Int) -> Unit)) : RecyclerView.Adapter<KeywordListAdapter.ViewHolder>() {


    var items: MutableList<KeywordDataItem> = mutableListOf()
    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val keywordName : Typography = view.findViewById(R.id.keyword_name)
        val keywordCount : Typography = view.findViewById(R.id.keyword_count)
        val keywordCompetition : Label = view.findViewById(R.id.keywordCompetition)
        val checkBox : CheckboxUnify = view.findViewById(R.id.checkBox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KeywordListAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.topads_create_layout_keyword_list_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    fun getSelectedItems(): List<KeywordDataItem> {
        val selected = mutableListOf<KeywordDataItem>()
        items.forEach { model ->
            if (model.onChecked) {
                selected.add(model)
            }
        }
        return selected
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.keywordName.text = items[holder.adapterPosition].keyword
        try {
            if (items[holder.adapterPosition].totalSearch == "-1") {
                holder.keywordCount.text = "  -  "

            } else
                holder.keywordCount.text = Utils.convertToCurrencyString(items[holder.adapterPosition].totalSearch.toLong())
        } catch (e: Exception) {
            holder.keywordCount.text = items[holder.adapterPosition].totalSearch.toString()
        }
        holder.checkBox.setOnCheckedChangeListener(null)
        holder.checkBox.isChecked = items[holder.adapterPosition].onChecked

        holder.view.setOnClickListener {
            holder.checkBox.isChecked = !holder.checkBox.isChecked
        }
        holder.checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (holder.adapterPosition != RecyclerView.NO_POSITION) {
                items[holder.adapterPosition].onChecked = isChecked
                onChecked.invoke(holder.adapterPosition)
            }
        }
        when (items[holder.adapterPosition].competition) {
            LOW -> {
                holder.keywordCompetition.setLabelType(Label.GENERAL_DARK_GREEN)
                holder.keywordCompetition.setLabel(holder.view.resources.getString(R.string.topads_common_keyword_competition_low))
            }

            MEDIUM -> {
                holder.keywordCompetition.setLabelType(Label.GENERAL_DARK_ORANGE)
                holder.keywordCompetition.setLabel(holder.view.resources.getString(R.string.topads_common_keyword_competition_moderation))
            }

            HIGH -> {
                holder.keywordCompetition.setLabelType(Label.GENERAL_DARK_RED)
                holder.keywordCompetition.setLabel(holder.view.resources.getString(R.string.topads_common_keyword_competition_high))
            }

            items[holder.adapterPosition].competition -> {
                holder.keywordCompetition.setLabelType(Label.GENERAL_DARK_GREY)
                holder.keywordCompetition.setLabel(holder.view.resources.getString(R.string.topads_common_keyword_competition_unknown))
            }

        }
    }

    fun setSelectedItem(selectedKeywordStage: MutableList<KeywordDataItem>?) {
        items.forEach {
            if(selectedKeywordStage?.contains(it)==true){
                it.onChecked = true
            }
        }
    }
}
