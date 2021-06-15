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
import kotlinx.android.synthetic.main.topads_create_layout_keyword_list_item.view.*


/**
 * Author errysuprayogi on 14,November,2019
 */
class KeywordListAdapter(private val onChecked: ((position: Int) -> Unit)) : RecyclerView.Adapter<KeywordListAdapter.ViewHolder>() {


    var items: MutableList<KeywordDataItem> = mutableListOf()
    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

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
        holder.view.keyword_name.text = items[holder.adapterPosition].keyword
        try {
            if (items[holder.adapterPosition].totalSearch == "-1") {
                holder.view.keyword_count.text = "  -  "

            } else
                holder.view.keyword_count.text = Utils.convertToCurrencyString(items[holder.adapterPosition].totalSearch.toLong())
        } catch (e: Exception) {
            holder.view.keyword_count.text = items[holder.adapterPosition].totalSearch.toString()
        }
        holder.view.checkBox.setOnCheckedChangeListener(null)
        holder.view.checkBox.isChecked = items[holder.adapterPosition].onChecked

        holder.view.setOnClickListener {
            holder.view.checkBox.isChecked = !holder.view.checkBox.isChecked
        }
        holder.view.checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (holder.adapterPosition != RecyclerView.NO_POSITION) {
                items[holder.adapterPosition].onChecked = isChecked
                onChecked.invoke(holder.adapterPosition)
            }
        }
        when (items[holder.adapterPosition].competition) {
            LOW -> {
                holder.view.keywordCompetition.setLabelType(Label.GENERAL_DARK_GREEN)
                holder.view.keywordCompetition.setLabel(holder.view.resources.getString(R.string.topads_common_keyword_competition_low))
            }

            MEDIUM -> {
                holder.view.keywordCompetition.setLabelType(Label.GENERAL_DARK_ORANGE)
                holder.view.keywordCompetition.setLabel(holder.view.resources.getString(R.string.topads_common_keyword_competition_moderation))
            }

            HIGH -> {
                holder.view.keywordCompetition.setLabelType(Label.GENERAL_DARK_RED)
                holder.view.keywordCompetition.setLabel(holder.view.resources.getString(R.string.topads_common_keyword_competition_high))
            }

            items[holder.adapterPosition].competition -> {
                holder.view.keywordCompetition.setLabelType(Label.GENERAL_DARK_GREY)
                holder.view.keywordCompetition.setLabel(holder.view.resources.getString(R.string.topads_common_keyword_competition_unknown))
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
