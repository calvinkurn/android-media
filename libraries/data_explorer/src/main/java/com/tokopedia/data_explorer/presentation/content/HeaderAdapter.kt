package com.tokopedia.data_explorer.presentation.content

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.data_explorer.domain.shared.models.Cell

internal class HeaderAdapter: RecyclerView.Adapter<ColumnHeaderViewHolder>() {

    private var headerItems: List<Cell> = listOf()
    var onClick: ((Cell) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColumnHeaderViewHolder {
        return ColumnHeaderViewHolder.getViewHolder(
            LayoutInflater.from(parent.context),
            parent,
            onClick
        )
    }

    override fun onBindViewHolder(holder: ColumnHeaderViewHolder, position: Int) {
        holder.bindHeader(headerItems[position])
    }

    override fun getItemCount() = headerItems.size

    fun setItems(cell: List<Cell>) {
        headerItems = cell
        notifyDataSetChanged()
    }
}