package com.tokopedia.db_inspector.presentation.schema

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.tokopedia.db_inspector.domain.shared.models.Cell

internal class SchemaAdapter(
    private val onClick : (String) -> Unit,
): ListAdapter<Cell, SchemaViewHolder>(CellDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SchemaViewHolder {
        return SchemaViewHolder.getViewHolder(
            LayoutInflater.from(parent.context),
            parent,
            onClick
        )
    }

    override fun onBindViewHolder(holder: SchemaViewHolder, position: Int) {
        holder.bindTable(currentList[position])
    }

}