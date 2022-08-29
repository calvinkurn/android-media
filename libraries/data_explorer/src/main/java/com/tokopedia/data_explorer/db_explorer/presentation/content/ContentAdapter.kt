package com.tokopedia.data_explorer.db_explorer.presentation.content

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.data_explorer.db_explorer.domain.shared.models.Cell
import com.tokopedia.data_explorer.db_explorer.presentation.schema.CellDiffUtil

internal class ContentAdapter : ListAdapter<Cell, RecyclerView.ViewHolder>(CellDiffUtil()) {

    var headersCount: Int = 0
    var onClick: ((Cell) -> Unit)? = null
    var headerItemClick: ((Cell) -> Unit)? = null

    companion object {
        const val TYPE_HEADER = 1
        const val TYPE_CONTENT = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_HEADER)
            ColumnHeaderViewHolder.getViewHolder(
                LayoutInflater.from(parent.context),
                parent,
                headerItemClick
            )
            else ContentViewHolder.getViewHolder(
                LayoutInflater.from(parent.context),
                parent,
                onClick
            )
      }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ColumnHeaderViewHolder) holder.bindHeader(currentList[position])
        else if (headersCount > 0 && holder is ContentViewHolder) {
            holder.bindCell(currentList[position], position / headersCount)
        }
    }

    override fun getItemViewType(position: Int) =
        if (position < headersCount) TYPE_HEADER else TYPE_CONTENT

    fun updateHeader(header: Cell) {

    }

}