package com.tokopedia.db_inspector.presentation.schema

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.db_inspector.R
import com.tokopedia.db_inspector.domain.shared.models.Cell
import kotlinx.android.synthetic.main.db_inspector_table_item_layout.view.*

internal class SchemaViewHolder(itemView: View, val onClick: (String) -> Unit) :
    RecyclerView.ViewHolder(itemView) {

        fun bindTable(cell: Cell) {
            with(itemView) {
                tableName.text = cell.text ?: "EMPTY"
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

        private val LAYOUT_ID = R.layout.db_inspector_table_item_layout
    }
}