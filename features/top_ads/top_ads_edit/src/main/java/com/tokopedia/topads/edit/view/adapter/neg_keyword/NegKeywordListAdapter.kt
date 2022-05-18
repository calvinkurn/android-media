package com.tokopedia.topads.edit.view.adapter.neg_keyword

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topads.common.data.response.GetKeywordResponse
import com.tokopedia.topads.edit.R
import com.tokopedia.topads.edit.utils.Constants.KEYWORD_EXISTS
import com.tokopedia.topads.edit.utils.Constants.KEYWORD_SOURCE
import com.tokopedia.topads.edit.utils.Constants.KEYWORD_TYPE_NEGATIVE_PHRASE
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by Pika on 13/4/20.
 */

class NegKeywordListAdapter(var onCheck: (() -> Unit?)) : RecyclerView.Adapter<NegKeywordListAdapter.ViewHolder>() {

    var items: MutableList<GetKeywordResponse.KeywordsItem> = mutableListOf()

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val keywordName : Typography = view.findViewById(R.id.keyword_name)
        val checkBox : CheckboxUnify = view.findViewById(R.id.checkBox)
    }

    fun getSelectedList(): List<GetKeywordResponse.KeywordsItem> {
        val selected: MutableList<GetKeywordResponse.KeywordsItem> = mutableListOf()

        items.forEach {
            if (it.isChecked) {
                selected.add(it)
            }
        }
        return selected
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.topads_edit_add_keyword_negative_item_layout, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.checkBox.setOnCheckedChangeListener(null)
        holder.checkBox.isChecked = items[holder.adapterPosition].isChecked
        holder.keywordName.text = items[holder.adapterPosition].tag

        holder.view.setOnClickListener {
            holder.checkBox.isChecked = !holder.checkBox.isChecked
        }
        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            items[holder.adapterPosition].isChecked = isChecked
            onCheck.invoke()
        }
    }

    fun addKeyword(name: String) {
        items.add(GetKeywordResponse.KeywordsItem(KEYWORD_TYPE_NEGATIVE_PHRASE, KEYWORD_EXISTS, "0", "0", true, name, KEYWORD_SOURCE))
        notifyItemInserted(items.size - 1)
    }
}