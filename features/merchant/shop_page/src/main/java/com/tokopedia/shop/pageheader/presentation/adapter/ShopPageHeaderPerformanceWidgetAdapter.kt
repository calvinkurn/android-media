package com.tokopedia.shop.pageheader.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.shop.pageheader.presentation.uimodel.component.BaseShopHeaderComponentUiModel

class ShopPageHeaderPerformanceWidgetAdapter(
    typeFactoryComponent: ShopPagePagePageHeaderPerformanceWidgetAdapterTypeFactory
) : BaseListAdapter<Visitable<*>, ShopPagePagePageHeaderPerformanceWidgetAdapterTypeFactory>(typeFactoryComponent) {

    fun addComponents(listComponent: List<BaseShopHeaderComponentUiModel>) {
        addElement(listComponent)
        notifyDataSetChanged()
    }
}
