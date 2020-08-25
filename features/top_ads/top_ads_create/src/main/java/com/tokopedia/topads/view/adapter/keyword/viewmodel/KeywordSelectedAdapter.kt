package com.tokopedia.topads.view.adapter.keyword.viewmodel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topads.Utils
import com.tokopedia.topads.create.R
import com.tokopedia.topads.data.response.KeywordDataItem
import com.tokopedia.topads.view.adapter.keyword.viewholder.KeywordItemViewHolder
import com.tokopedia.unifycomponents.Label
import kotlinx.android.synthetic.main.topads_create_layout_keyword_list_item.view.*
import java.lang.Exception

/**
 * Created by Pika on 20/8/20.
 */

class KeywordSelectedAdapter(private val onChecked:((position:Int)->Unit)) : RecyclerView.Adapter<KeywordSelectedAdapter.ViewHolder>() {

    var items: MutableList<KeywordDataItem> = mutableListOf()
    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.topads_create_layout_keyword_list_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return items.count()
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.view.keyword_name.text = items[position].keyword
        try {
            holder.view.keyword_count.text = Utils.convertToCurrencyString(items[position].totalSearch.toLong())
        } catch (e: Exception) {
            holder.view.keyword_count.text = items[position].totalSearch.toString()
        }
        holder.view.checkBox.setOnCheckedChangeListener(null)
        holder.view.checkBox.isChecked = true
        holder.view.setOnClickListener {
         //   holder.view.checkBox.setOnCheckedChangeListener(null)
            holder.view.checkBox.isChecked =false
        }
        holder.view.checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            items[position].onChecked = isChecked
            onChecked.invoke(position)

        }
        when (items[position].competition) {
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