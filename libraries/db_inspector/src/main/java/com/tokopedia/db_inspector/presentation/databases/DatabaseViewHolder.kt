package com.tokopedia.db_inspector.presentation.databases

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.db_inspector.R
import com.tokopedia.db_inspector.domain.databases.models.DatabaseDescriptor
import com.tokopedia.unifycomponents.Toaster
import kotlinx.android.synthetic.main.db_inspector_database_item_layout.view.*

internal class DatabaseViewHolder(itemView: View, val onClick: (DatabaseDescriptor) -> Unit) : RecyclerView.ViewHolder(itemView) {

    fun bindDatabase(databaseDescriptor: DatabaseDescriptor) {
        with(itemView) {
            databaseName.text = databaseDescriptor.name
            databasePath.text = databaseDescriptor.parentPath
            databaseVersionNumber.text = databaseDescriptor.version
            databaseAction.setOnClickListener {
                Toaster.build(databaseAction, "Action", Toaster.LENGTH_SHORT, Toaster.TYPE_NORMAL).show()
            }
        }
        itemView.setOnClickListener { onClick(databaseDescriptor) }

    }

    companion object {
        private val LAYOUT_ID = R.layout.db_inspector_database_item_layout
        fun getViewHolder(
            inflater: LayoutInflater,
            parent: ViewGroup,
            onClick: (DatabaseDescriptor) -> Unit
        ) = DatabaseViewHolder(
            inflater.inflate(LAYOUT_ID, parent, false), onClick
        )
    }
}