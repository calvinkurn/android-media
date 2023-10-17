package com.tokopedia.oldcatalog.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.oldcatalog.model.datamodel.BaseCatalogDataModel

class CatalogDetailDiffUtil: DiffUtil.ItemCallback<BaseCatalogDataModel>() {

    override fun areItemsTheSame(oldItem: BaseCatalogDataModel, newItem: BaseCatalogDataModel): Boolean {
        return oldItem.name() == newItem.name()
    }

    override fun areContentsTheSame(oldItem: BaseCatalogDataModel, newItem: BaseCatalogDataModel): Boolean {
        return oldItem.equalsWith(newItem)
    }

    override fun getChangePayload(oldItem: BaseCatalogDataModel, newItem: BaseCatalogDataModel): Any? {
        return oldItem.getChangePayload(newItem)
    }
}
