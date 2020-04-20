package com.tokopedia.topads.edit.view.adapter.neg_keyword

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topads.edit.R
import com.tokopedia.topads.edit.data.param.NegKeyword
import kotlinx.android.synthetic.main.topads_edit_add_keyword_negative_item_layout.view.*

/**
 * Created by Pika on 13/4/20.
 */

class NegKeywordListAdapter(var onCheck: (() -> Unit?)) : RecyclerView.Adapter<NegKeywordListAdapter.ViewHolder>() {

    var items: MutableList<NegKeyword> = mutableListOf()

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        fun viewHolder(itemView: View) {
            super.itemView
        }

        override fun onClick(view: View?) {

            view?.setOnClickListener {
                it.checkBox.isChecked = !it.checkBox.isChecked
            }

        }
    }

    fun getSelectedList(): List<NegKeyword> {
        val selected: MutableList<NegKeyword> = mutableListOf()

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
        holder.view.checkBox.setOnCheckedChangeListener(null)
        holder.view.checkBox.isChecked = items[position].isChecked
        holder.view.keyword_name.text = items[position].name
        holder.view.checkBox.setOnCheckedChangeListener { _, isChecked ->
            items[position].isChecked = isChecked
            onCheck.invoke()
        }
    }

    fun addKeyword(name: String) {
        val model = NegKeyword()
        model.isChecked = true
        model.name = name
        items.add(model)
        items[items.size - 1].isChecked = true
        notifyItemInserted(items.size - 1)
    }


}