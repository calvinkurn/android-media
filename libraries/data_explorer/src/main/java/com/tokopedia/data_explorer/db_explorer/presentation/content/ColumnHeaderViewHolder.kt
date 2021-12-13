package com.tokopedia.data_explorer.db_explorer.presentation.content

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.data_explorer.R
import com.tokopedia.data_explorer.db_explorer.domain.shared.models.Cell
import kotlinx.android.synthetic.main.data_explorer_table_header_item_layout.view.*

internal class ColumnHeaderViewHolder(itemView: View, val onClick: ((Cell) -> Unit)?): RecyclerView.ViewHolder(itemView) {

    fun bindHeader(cell: Cell) {
        with(itemView) {
            columnName.text = cell.text
        }
        itemView.setOnClickListener { onClick?.invoke(cell) }
    }

    companion object {
        private val LAYOUT_ID = R.layout.data_explorer_table_header_item_layout

        fun getViewHolder(
            inflater: LayoutInflater,
            parent: ViewGroup,
            onClick: ((Cell) -> Unit)?
        ) = ColumnHeaderViewHolder(
            inflater.inflate(
                LAYOUT_ID,
                parent,
                false
            ), onClick
        )
    }

}
