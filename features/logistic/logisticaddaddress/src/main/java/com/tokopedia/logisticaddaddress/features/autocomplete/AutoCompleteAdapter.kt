package com.tokopedia.logisticaddaddress.features.autocomplete

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.autocomplete.AutoCompleteResultUi
import kotlinx.android.synthetic.main.item_autocomplete_result.view.*

class AutoCompleteAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val data: MutableList<AutoCompleteVisitable> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return ResultViewHolder(view)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = data[position]
        when (holder) {
            is ResultViewHolder -> holder.bind(item as AutoCompleteResultUi)
        }
    }

    override fun getItemViewType(position: Int): Int = when (data[position]) {
        is AutoCompleteResultUi -> R.layout.item_autocomplete_result
        else -> throw RuntimeException("View type not found!!")
    }

    fun setData(items: List<AutoCompleteResultUi>) {
        data.clear()
        data.addAll(items)
        notifyDataSetChanged()
    }

    interface AutoCompleteVisitable

    class ResultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: AutoCompleteResultUi) {
            itemView.tv_autocomplete_title.text = item.structuredFormatting.mainText
            itemView.tv_autocomplete_desc.text = item.structuredFormatting.secondaryText
        }
    }
}