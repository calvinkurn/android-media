package com.tokopedia.tokopedianow.home.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.common.base.adapter.BaseTokopediaNowListAdapter
import com.tokopedia.tokopedianow.home.presentation.adapter.differ.HomeListDiffer
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeChooseAddressWidgetUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeTickerUiModel

class HomeAdapter(
        typeFactory: HomeAdapterTypeFactory,
        differ: HomeListDiffer
) : BaseTokopediaNowListAdapter<Visitable<*>, HomeAdapterTypeFactory>(typeFactory, differ) {

    fun removeHomeChooseAddressWidget() {
        val items = data.toMutableList()
        val widget = getItem(HomeChooseAddressWidgetUiModel::class.java)
        widget?.let {
            items.remove(it)
            submitList(items)
        }
    }

    fun removeTickerWidget() {
        val items = data.toMutableList()
        val widget = getItem(HomeTickerUiModel::class.java)
        widget?.let {
            items.remove(it)
            submitList(items)
        }
    }

    fun <T> getItem(itemClass: Class<T>): Visitable<*>? {
        return data.find { it.javaClass == itemClass}
    }
}