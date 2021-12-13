package com.tokopedia.data_explorer.db_explorer.presentation.schema

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.data_explorer.R
import com.tokopedia.data_explorer.db_explorer.domain.shared.models.Cell
import kotlinx.android.synthetic.main.data_explorer_table_item_layout.view.*

internal class SchemaViewHolder(itemView: View, val onClick: (String) -> Unit) :
    RecyclerView.ViewHolder(itemView) {

        fun bindTable(cell: Cell) {
            with(itemView) {
                tableName.text = cell.text ?: "EMPTY"
                setOnClickListener { cell.text?.let { onClick(it) } }
            }
        }

    companion object {
        fun getViewHolder(
            inflater: LayoutInflater,
            parent: ViewGroup,
            onClick: (String) -> Unit
        ) = SchemaViewHolder(
            inflater.inflate(LAYOUT_ID, parent, false), onClick
        )

        private val LAYOUT_ID = R.layout.data_explorer_table_item_layout
    }
}