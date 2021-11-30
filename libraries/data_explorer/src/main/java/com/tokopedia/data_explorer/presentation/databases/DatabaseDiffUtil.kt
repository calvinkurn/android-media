package com.tokopedia.data_explorer.presentation.databases

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.data_explorer.domain.databases.models.DatabaseDescriptor

internal class DatabaseDiffUtil : DiffUtil.ItemCallback<DatabaseDescriptor>() {

    override fun areItemsTheSame(oldItem: DatabaseDescriptor, newItem: DatabaseDescriptor): Boolean {
        return oldItem.absolutePath == newItem.absolutePath
    }

    override fun areContentsTheSame(oldItem: DatabaseDescriptor, newItem: DatabaseDescriptor): Boolean {
        return oldItem == newItem
    }
}
