package com.tokopedia.data_explorer.db_explorer.presentation.content.bottomsheet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.data_explorer.R
import kotlinx.android.synthetic.main.data_explorer_column_selector_item_layout.view.*

internal class ColumnSelectorAdapter(private val columnList: ArrayList<String>): RecyclerView.Adapter<ColumnSelectorAdapter.ColumnSelectorViewHolder>() {

    var lastCheckedColumnIndex = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):ColumnSelectorAdapter.ColumnSelectorViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.data_explorer_column_selector_item_layout,
                parent,
                false
        )
        return ColumnSelectorViewHolder(view)
    }

    override fun onBindViewHolder(holder: ColumnSelectorAdapter.ColumnSelectorViewHolder , position: Int) {
        holder.bind(columnList[position])
        holder.itemView.columnRadio.isChecked = lastCheckedColumnIndex == position
    }

    override fun getItemCount() = columnList.size

    inner class ColumnSelectorViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(text: String) {
            itemView.columnRadio.text = text
            itemView.setOnClickListener {
                lastCheckedColumnIndex = adapterPosition
                notifyDataSetChanged()
            }
        }
    }
}