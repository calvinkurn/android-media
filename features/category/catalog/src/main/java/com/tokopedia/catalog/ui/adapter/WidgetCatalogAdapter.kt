package com.tokopedia.catalog.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable


class WidgetCatalogAdapter(
    baseListAdapterTypeFactory: CatalogAdapterFactoryImpl,
) : BaseCatalogAdapter<Visitable<*>, CatalogAdapterFactoryImpl>(
    baseListAdapterTypeFactory) {

    private val differ = CatalogDifferImpl()

    override fun addWidget(itemList: List<Visitable<*>>) {
        val diffUtilCallback = differ.create(visitables, itemList)
        val result = DiffUtil.calculateDiff(diffUtilCallback)
        visitables.clear()
        visitables.addAll(itemList)
        result.dispatchUpdatesTo(this)
    }

}
