package com.tokopedia.data_explorer.db_explorer.presentation.databases

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.tokopedia.data_explorer.db_explorer.domain.databases.models.DatabaseDescriptor

internal class DatabaseAdapter(
    private val onClick: (DatabaseDescriptor) -> Unit,
): ListAdapter<DatabaseDescriptor, DatabaseViewHolder>(DatabaseDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DatabaseViewHolder {
        return DatabaseViewHolder.getViewHolder(
            LayoutInflater.from(parent.context),
            parent,
            onClick
        )
    }

    override fun onBindViewHolder(holder: DatabaseViewHolder, position: Int) {
        holder.bindDatabase(currentList[position])
    }

}