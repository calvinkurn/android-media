package com.tokopedia.data_explorer.db_explorer.presentation.content

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.data_explorer.R
import com.tokopedia.data_explorer.db_explorer.data.models.cursor.input.Order
import com.tokopedia.data_explorer.db_explorer.domain.shared.models.Cell
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import kotlinx.android.synthetic.main.data_explorer_table_header_item_layout.view.*

internal class ColumnHeaderViewHolder(itemView: View, val onClick: ((Cell) -> Unit)?): RecyclerView.ViewHolder(itemView) {

    fun bindHeader(cell: Cell) {
        val nextSort = if (cell.order == Order.ASCENDING) Order.DESCENDING else Order.ASCENDING

        with(itemView) {
            columnName.text = cell.text
            if (cell.active) {
                sortIcon.setImage(nextSort.icon)
                sortIcon.visible()
            } else sortIcon.gone()
        }
        itemView.setOnClickListener { onClick?.invoke(cell.copy(order = nextSort)) }
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
