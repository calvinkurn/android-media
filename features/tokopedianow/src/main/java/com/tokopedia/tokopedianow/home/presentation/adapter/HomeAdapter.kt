package com.tokopedia.tokopedianow.home.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.common.base.adapter.BaseTokopediaNowListAdapter
import com.tokopedia.tokopedianow.common.model.TokoNowChooseAddressWidgetUiModel
import com.tokopedia.tokopedianow.home.presentation.adapter.differ.HomeListDiffer

class HomeAdapter(
        typeFactory: HomeAdapterTypeFactory,
        differ: HomeListDiffer
) : BaseTokopediaNowListAdapter<Visitable<*>, HomeAdapterTypeFactory>(typeFactory, differ) {

    fun removeHomeChooseAddressWidget() {
        val items = data.toMutableList()
        val widget = getItem(TokoNowChooseAddressWidgetUiModel::class.java)
        items.remove(widget)
        submitList(items)
    }

    inline fun <reified T: Visitable<*>> getItem(itemClass: Class<T>): T? {
        return data.find { it.javaClass == itemClass } as T?
    }

    fun findPosition(visitable: Visitable<*>): Int {
        return data.indexOf(visitable)
    }
}
