package com.tokopedia.tokomart.home.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokomart.home.base.adapter.BaseHomeListAdapter
import com.tokopedia.tokomart.home.presentation.adapter.differ.TokoMartHomeListDiffer
import com.tokopedia.tokomart.home.presentation.uimodel.HomeChooseAddressWidgetUiModel

class TokoMartHomeAdapter(
    typeFactory: TokoMartHomeAdapterTypeFactory,
    differ: TokoMartHomeListDiffer
): BaseHomeListAdapter<Visitable<*>, TokoMartHomeAdapterTypeFactory>(typeFactory, differ) {

    fun updateHomeChooseAddressWidget(homeChooseAddressWidgetUiModel: HomeChooseAddressWidgetUiModel) {
        val items = data.toMutableList()
        val index = items.find { it is HomeChooseAddressWidgetUiModel }?.let { firstIndex }
        index?.let {
            items[it] = homeChooseAddressWidgetUiModel
            submitList(items)
        }
    }

    fun removeHomeChooseAddressWidget() {
        val items = data.toMutableList()
        val index = items.find { it is HomeChooseAddressWidgetUiModel }?.let { firstIndex }
        index?.let {
            items.removeAt(it)
            submitList(items)
        }
    }
}