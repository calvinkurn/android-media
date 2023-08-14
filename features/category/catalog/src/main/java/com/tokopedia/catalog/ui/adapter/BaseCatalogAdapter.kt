package com.tokopedia.catalog.ui.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory

abstract class BaseCatalogAdapter<T, F: AdapterTypeFactory>(
    baseListAdapterTypeFactory: F
): BaseListAdapter<T, F>(baseListAdapterTypeFactory) {

    abstract fun addWidget(itemList: List<Visitable<*>>)

}
