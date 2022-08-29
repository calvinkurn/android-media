package com.tokopedia.data_explorer.db_explorer.presentation.content.bottomsheet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.data_explorer.R
import com.tokopedia.data_explorer.db_explorer.data.models.cursor.input.Order
import com.tokopedia.data_explorer.db_explorer.domain.shared.models.Cell
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import kotlinx.android.synthetic.main.data_explorer_column_selector_item_layout.view.*
import kotlinx.android.synthetic.main.data_explorer_table_header_item_layout.view.*

internal class ColumnSelectorViewHolder(itemView: View, val onClick: ((String) -> Unit)?): RecyclerView.ViewHolder(itemView) {

    fun bindColumn(columnName: String) {
        itemView.apply {
            columnRadio.text = columnName
        }
    }

    companion object {
        private val LAYOUT_ID = R.layout.data_explorer_column_selector_item_layout

        fun getViewHolder(
            inflater: LayoutInflater,
            parent: ViewGroup,
            onClick: ((String) -> Unit)?
        ) = ColumnSelectorViewHolder(
            inflater.inflate(
                LAYOUT_ID,
                parent,
                false
            ), onClick
        )
    }

}
