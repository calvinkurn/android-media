package com.tokopedia.catalog_library.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.catalog_library.model.datamodel.BaseCatalogLibraryDM

class CatalogLibraryDiffUtil : DiffUtil.ItemCallback<BaseCatalogLibraryDM>() {
    override fun areItemsTheSame(
        oldItem: BaseCatalogLibraryDM,
        newItem: BaseCatalogLibraryDM
    ): Boolean {
        return oldItem.type() == newItem.type()
    }

    override fun areContentsTheSame(
        oldItem: BaseCatalogLibraryDM,
        newItem: BaseCatalogLibraryDM
    ): Boolean {
        return oldItem.equalsWith(newItem)
    }

    override fun getChangePayload(
        oldItem: BaseCatalogLibraryDM,
        newItem: BaseCatalogLibraryDM
    ): Any? {
        return oldItem.getChangePayload(newItem)
    }
}
