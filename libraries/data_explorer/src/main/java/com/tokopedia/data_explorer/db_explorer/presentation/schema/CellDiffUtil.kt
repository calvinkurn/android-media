package com.tokopedia.data_explorer.db_explorer.presentation.schema

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.data_explorer.db_explorer.domain.shared.models.Cell

internal class CellDiffUtil : DiffUtil.ItemCallback<Cell>() {

    override fun areItemsTheSame(oldItem: Cell, newItem: Cell): Boolean {
        return false
    }

    override fun areContentsTheSame(oldItem: Cell, newItem: Cell): Boolean {
        return oldItem == newItem
    }
}
