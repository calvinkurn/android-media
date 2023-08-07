package com.tokopedia.data_explorer.db_explorer.presentation.content

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.data_explorer.R
import com.tokopedia.data_explorer.db_explorer.domain.shared.models.Cell
import kotlinx.android.synthetic.main.data_explorer_table_cell_item_layout.view.*

internal class ContentViewHolder(itemView: View, val onClick: ((Cell) -> Unit)?) :
    RecyclerView.ViewHolder(itemView) {

    fun bindCell(cell: Cell, row: Int) {
        with(itemView) {
            cellText.text = cell.text
            this.rootView.setBackgroundColor(
                MethodChecker.getColor(
                    context,
                    if (row % 2 == 0) com.tokopedia.unifyprinciples.R.color.Unify_NN50
                    else com.tokopedia.unifycomponents.R.color.Unify_Background
                )
            )
        }
        itemView.setOnClickListener { onClick?.invoke(cell) }
    }

    companion object {
        private val LAYOUT_ID = R.layout.data_explorer_table_cell_item_layout

        fun getViewHolder(
            inflater: LayoutInflater,
            parent: ViewGroup,
            onClick: ((Cell) -> Unit)?
        ) = ContentViewHolder(
            inflater.inflate(LAYOUT_ID, parent, false), onClick
        )
    }

}
