package com.tokopedia.db_inspector.presentation.databases

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.db_inspector.domain.databases.models.DatabaseDescriptor

internal class DatabaseDiffUtil : DiffUtil.ItemCallback<DatabaseDescriptor>() {

    override fun areItemsTheSame(oldItem: DatabaseDescriptor, newItem: DatabaseDescriptor): Boolean {
        return oldItem.absolutePath == newItem.absolutePath
    }

    override fun areContentsTheSame(oldItem: DatabaseDescriptor, newItem: DatabaseDescriptor): Boolean {
        return oldItem == newItem
    }
}
