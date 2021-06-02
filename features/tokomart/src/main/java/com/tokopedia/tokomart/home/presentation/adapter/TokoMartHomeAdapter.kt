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

    fun updateHomeChooseAddressWidget(isRefresh: Boolean) {
        val items = data.toMutableList()
        val index = items.find { it is HomeChooseAddressWidgetUiModel }?.let { firstIndex }
        index?.let {
            items[it] = (items[it] as HomeChooseAddressWidgetUiModel).copy(isRefresh = isRefresh)
            submitList(items)
        }
    }

    fun removeHomeChooseAddressWidget() {
        val items = data.toMutableList()
        val widget = items.find { it is HomeChooseAddressWidgetUiModel }?.apply { firstIndex }
        widget?.let {
            items.remove(it)
            submitList(items)
        }
    }

    fun removeTickerWidget() {
        val items = data.toMutableList()
        val widget = items.find { it is HomeTickerUiModel }?.apply { firstIndex }
        widget?.let {
            items.remove(it)
            submitList(items)
        }
    }
}