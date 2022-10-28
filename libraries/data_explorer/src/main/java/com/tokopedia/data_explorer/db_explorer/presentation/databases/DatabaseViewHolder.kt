package com.tokopedia.data_explorer.db_explorer.presentation.databases

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.data_explorer.R
import com.tokopedia.data_explorer.db_explorer.domain.databases.models.DatabaseDescriptor
import com.tokopedia.data_explorer.db_explorer.domain.databases.models.DatabaseInteractions
import kotlinx.android.synthetic.main.data_explorer_database_item_layout.view.*

internal class DatabaseViewHolder(
    itemView: View, val onClick: (DatabaseDescriptor) -> Unit,
    private val interactions: DatabaseInteractions
) : RecyclerView.ViewHolder(itemView) {

    fun bindDatabase(databaseDescriptor: DatabaseDescriptor) {
        with(itemView) {
            databaseName.text = databaseDescriptor.name
            databasePath.text = databaseDescriptor.parentPath
            databaseVersionNumber.text = databaseDescriptor.version
            copyDatabase.setOnClickListener { interactions.onCopy(databaseDescriptor) }
            shareDatabase.setOnClickListener { interactions.onShare(databaseDescriptor) }
            deleteDatabase.setOnClickListener { interactions.onDelete(databaseDescriptor) }
            deleteDatabase.isEnabled = databaseDescriptor.isDeletable
        }
        itemView.setOnClickListener { onClick(databaseDescriptor) }

    }

    companion object {
        private val LAYOUT_ID = R.layout.data_explorer_database_item_layout
        fun getViewHolder(
            inflater: LayoutInflater,
            parent: ViewGroup,
            onClick: (DatabaseDescriptor) -> Unit,
            interactions: DatabaseInteractions
        ) = DatabaseViewHolder(
            inflater.inflate(LAYOUT_ID, parent, false), onClick, interactions
        )
    }
}