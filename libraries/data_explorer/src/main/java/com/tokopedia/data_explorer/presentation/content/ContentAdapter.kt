package com.tokopedia.data_explorer.presentation.content

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.tokopedia.data_explorer.domain.shared.models.Cell
import com.tokopedia.data_explorer.presentation.schema.CellDiffUtil

internal class ContentAdapter : ListAdapter<Cell, ContentViewHolder>(CellDiffUtil()) {

    var headersCount: Int = 0
    var onClick: ((Cell) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentViewHolder {
        return ContentViewHolder.getViewHolder(
            LayoutInflater.from(parent.context),
            parent,
            onClick
        )
    }

    override fun onBindViewHolder(holder: ContentViewHolder, position: Int) {
        if (headersCount > 0) {
            holder.bindCell(currentList[position], position / headersCount)
        }
    }
}