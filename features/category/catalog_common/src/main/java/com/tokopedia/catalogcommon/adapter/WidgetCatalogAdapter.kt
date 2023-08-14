package com.tokopedia.catalogcommon.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.catalogcommon.adapter.BaseCatalogAdapter
import com.tokopedia.catalogcommon.adapter.CatalogAdapterFactoryImpl
import com.tokopedia.catalogcommon.adapter.CatalogDifferImpl


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
