package com.tokopedia.shop.pageheader.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.shop.pageheader.presentation.uimodel.component.BaseShopPageHeaderComponentUiModel

class ShopPageHeaderPerformanceWidgetAdapter(
    typeFactoryComponent: ShopPageHeaderPerformanceWidgetAdapterTypeFactory
) : BaseListAdapter<Visitable<*>, ShopPageHeaderPerformanceWidgetAdapterTypeFactory>(typeFactoryComponent) {

    fun addComponents(listComponentPage: List<BaseShopPageHeaderComponentUiModel>) {
        addElement(listComponentPage)
        notifyDataSetChanged()
    }
}
