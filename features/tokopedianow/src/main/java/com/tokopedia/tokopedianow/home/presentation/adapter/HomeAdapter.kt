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
        widget?.let {
            items.remove(it)
            submitList(items)
        }
    }

    fun <T> getItem(itemClass: Class<T>): Visitable<*>? {
        return data.find { it.javaClass == itemClass}
    }

    fun findPosition(visitable: Visitable<*>): Int {
        return data.indexOf(visitable)
    }
}