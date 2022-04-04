package com.tokopedia.topads.edit.view.adapter.keyword

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topads.common.data.response.KeywordDataItem
import com.tokopedia.topads.common.data.util.Utils
import com.tokopedia.topads.edit.R
import com.tokopedia.topads.edit.view.adapter.keyword.viewholder.KeywordItemViewHolder
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by Pika on 23/8/20.
 */

class KeywordSelectedAdapter(private val onChecked: ((position: Int) -> Unit)) :
    RecyclerView.Adapter<KeywordSelectedAdapter.ViewHolder>() {

    var items: MutableList<KeywordDataItem> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.topads_edit_layout_keyword_list_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.keywordName.text = items[holder.adapterPosition].keyword
        try {
            if (items[holder.adapterPosition].totalSearch == "-1") {
                holder.keywordCount.text = "  -  "

            } else
                holder.keywordCount.text =
                    Utils.convertToCurrencyString(items[position].totalSearch.toLong())
        } catch (e: Exception) {
            holder.keywordCount.text = items[holder.adapterPosition].totalSearch.toString()
        }
        holder.checkBox.setOnCheckedChangeListener(null)
        holder.checkBox.isChecked = true
        holder.view.setOnClickListener {
            holder.checkBox.isChecked = false
        }
        holder.checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (holder.adapterPosition != RecyclerView.NO_POSITION) {
                items[holder.adapterPosition].onChecked = isChecked
                onChecked.invoke(holder.adapterPosition)
            }
        }
        if (items[holder.adapterPosition].competition.isEmpty()) {
            holder.keywordCompetition.setLabelType(Label.GENERAL_DARK_GREY)
            holder.keywordCompetition.setLabel(holder.view.resources.getString(R.string.topads_common_keyword_competition_unknown))

        } else {
            holder.keywordCompetition.visibility = View.VISIBLE
            when (items[holder.adapterPosition].competition) {
                KeywordItemViewHolder.LOW -> {
                    holder.keywordCompetition.setLabelType(Label.GENERAL_DARK_GREEN)
                    holder.keywordCompetition.setLabel(holder.view.resources.getString(R.string.topads_common_keyword_competition_low))
                }

                KeywordItemViewHolder.MEDIUM -> {
                    holder.keywordCompetition.setLabelType(Label.GENERAL_DARK_ORANGE)
                    holder.keywordCompetition.setLabel(holder.view.resources.getString(R.string.topads_common_keyword_competition_moderation))
                }

                KeywordItemViewHolder.HIGH -> {
                    holder.keywordCompetition.setLabelType(Label.GENERAL_DARK_RED)
                    holder.keywordCompetition.setLabel(holder.view.resources.getString(R.string.topads_common_keyword_competition_high))
                }

            }
        }
    }

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val keywordName: Typography = view.findViewById(R.id.keyword_name)
        val keywordCount: Typography = view.findViewById(R.id.keyword_count)
        val keywordCompetition: Label = view.findViewById(R.id.keywordCompetition)
        val checkBox: CheckboxUnify = view.findViewById(R.id.checkBox)
    }

}