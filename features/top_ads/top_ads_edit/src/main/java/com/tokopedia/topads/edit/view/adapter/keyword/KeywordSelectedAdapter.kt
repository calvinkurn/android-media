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
import kotlinx.android.synthetic.main.topads_edit_layout_keyword_list_item.view.*

/**
 * Created by Pika on 23/8/20.
 */

class KeywordSelectedAdapter(private val onChecked: ((position: Int) -> Unit)) : RecyclerView.Adapter<KeywordSelectedAdapter.ViewHolder>() {

    var items: MutableList<KeywordDataItem> = mutableListOf()

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.topads_edit_layout_keyword_list_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return items.count()
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.view.keyword_name.text = items[holder.adapterPosition].keyword
        try {
            if (items[holder.adapterPosition].totalSearch == "-1") {
                holder.view.keyword_count.text = "  -  "

            } else
                holder.view.keyword_count.text = Utils.convertToCurrencyString(items[position].totalSearch.toLong())
        } catch (e: Exception) {
            holder.view.keyword_count.text = items[holder.adapterPosition].totalSearch.toString()
        }
        holder.view.checkBox.setOnCheckedChangeListener(null)
        holder.view.checkBox.isChecked = true
        holder.view.setOnClickListener {
            holder.view.checkBox.isChecked = false
        }
        holder.view.checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (holder.adapterPosition != RecyclerView.NO_POSITION) {
                items[holder.adapterPosition].onChecked = isChecked
                onChecked.invoke(holder.adapterPosition)
            }

        }
        if (items[holder.adapterPosition].competition.isEmpty()) {
            holder.view.keywordCompetition.setLabel(holder.view.resources.getString(R.string.topads_common_keyword_competition_low))
            holder.view.keywordCompetition.visibility = View.INVISIBLE


        } else {
            holder.view.keywordCompetition.visibility = View.VISIBLE
            when (items[holder.adapterPosition].competition) {
                KeywordItemViewHolder.LOW -> {
                    holder.view.keywordCompetition.setLabelType(Label.GENERAL_DARK_GREEN)
                    holder.view.keywordCompetition.setLabel(holder.view.resources.getString(R.string.topads_common_keyword_competition_low))
                }

                KeywordItemViewHolder.MEDIUM -> {
                    holder.view.keywordCompetition.setLabelType(Label.GENERAL_DARK_ORANGE)
                    holder.view.keywordCompetition.setLabel(holder.view.resources.getString(R.string.topads_common_keyword_competition_moderation))
                }

                KeywordItemViewHolder.HIGH -> {
                    holder.view.keywordCompetition.setLabelType(Label.GENERAL_DARK_RED)
                    holder.view.keywordCompetition.setLabel(holder.view.resources.getString(R.string.topads_common_keyword_competition_high))
                }

            }
        }
    }

}