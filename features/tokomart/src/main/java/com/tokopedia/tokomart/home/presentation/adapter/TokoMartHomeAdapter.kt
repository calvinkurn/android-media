package com.tokopedia.tokomart.home.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokomart.common.base.adapter.BaseTokoMartListAdapter
import com.tokopedia.tokomart.home.presentation.adapter.differ.TokoMartHomeListDiffer
import com.tokopedia.tokomart.home.presentation.uimodel.HomeChooseAddressWidgetUiModel
import com.tokopedia.tokomart.home.presentation.uimodel.HomeTickerUiModel

class TokoMartHomeAdapter(
    typeFactory: TokoMartHomeAdapterTypeFactory,
    differ: TokoMartHomeListDiffer
) : BaseTokoMartListAdapter<Visitable<*>, TokoMartHomeAdapterTypeFactory>(typeFactory, differ) {

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