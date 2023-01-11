package com.tokopedia.catalog_library.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.catalog_library.model.datamodel.BaseCatalogLibraryDataModel

class CatalogLibraryDiffUtil : DiffUtil.ItemCallback<BaseCatalogLibraryDataModel>() {
    override fun areItemsTheSame(
        oldItem: BaseCatalogLibraryDataModel,
        newItem: BaseCatalogLibraryDataModel
    ): Boolean {
        return oldItem.type() == newItem.type()
    }

    override fun areContentsTheSame(
        oldItem: BaseCatalogLibraryDataModel,
        newItem: BaseCatalogLibraryDataModel
    ): Boolean {
        return oldItem.equalsWith(newItem)
    }

    override fun getChangePayload(
        oldItem: BaseCatalogLibraryDataModel,
        newItem: BaseCatalogLibraryDataModel
    ): Any? {
        return oldItem.getChangePayload(newItem)
    }
}
