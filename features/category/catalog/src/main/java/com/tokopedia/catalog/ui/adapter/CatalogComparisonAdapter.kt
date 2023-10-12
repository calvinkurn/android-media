package com.tokopedia.catalog.ui.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter

class CatalogComparisonAdapter(
    private val baseListAdapterTypeFactory: CatalogComparisonDetailAdapterFactoryImpl
) : BaseAdapter<CatalogComparisonDetailAdapterFactoryImpl>(
    baseListAdapterTypeFactory
) {

    fun addWidget(itemList: List<Visitable<*>>) {
        visitables.clear()
        visitables.addAll(itemList)
    }
}
