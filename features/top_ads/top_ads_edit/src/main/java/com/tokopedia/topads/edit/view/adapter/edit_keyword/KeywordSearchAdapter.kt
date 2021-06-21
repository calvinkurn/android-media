package com.tokopedia.topads.edit.view.adapter.edit_keyword

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topads.common.data.response.SearchData
import com.tokopedia.topads.common.data.util.Utils
import com.tokopedia.topads.edit.R
import com.tokopedia.topads.edit.view.adapter.keyword.viewholder.KeywordItemViewHolder
import com.tokopedia.unifycomponents.Label
import kotlinx.android.synthetic.main.topads_edit_layout_keyword_list_item.view.*

/**
 * Created by Pika on 20/8/20.
 */

class KeywordSearchAdapter(private val onChecked: (() -> Unit)) : RecyclerView.Adapter<KeywordSearchAdapter.ViewHolder>() {
    var items: MutableList<SearchData> = mutableListOf()


    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.topads_edit_layout_keyword_list_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return items.count()
    }


    fun getSelectedItem(): MutableList<SearchData> {
        val list: MutableList<SearchData> = mutableListOf()
        items.forEach {
            if (it.onChecked) {
                list.add(it)
            }

        }
        return list
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.view.keyword_name.text = items[holder.adapterPosition].keyword
        try {
            if (items[holder.adapterPosition].totalSearch == -1) {
                holder.view.keyword_count.text = "  -  "

            } else
                holder.view.keyword_count.text = Utils.convertToCurrencyString(items[position].totalSearch.toLong())
        } catch (e: Exception) {
            holder.view.keyword_count.text = items[holder.adapterPosition].totalSearch.toString()
        }
        holder.view.checkBox.setOnCheckedChangeListener(null)
        holder.view.checkBox.isChecked = items[holder.adapterPosition].onChecked

        holder.view.setOnClickListener {
            holder.view.checkBox.isChecked = !holder.view.checkBox.isChecked
        }
        holder.view.checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            items[holder.adapterPosition].onChecked = isChecked
            onChecked.invoke()
        }
        if ((items[holder.adapterPosition].competition ?: "").isEmpty()) {
            holder.view.keywordCompetition.setLabelType(Label.GENERAL_DARK_GREY)
            holder.view.keywordCompetition.setLabel(holder.view.resources.getString(R.string.topads_common_keyword_competition_unknown))

        } else {
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