package com.tokopedia.shop.pageheader.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.shop.pageheader.presentation.uimodel.component.BaseShopHeaderComponentUiModel

class ShopActionButtonWidgetAdapter(
        typeFactoryComponent: ShopHeaderActionButtonWidgetAdapterTypeFactory
):  BaseListAdapter<Visitable<*>, ShopHeaderActionButtonWidgetAdapterTypeFactory>(typeFactoryComponent) {

    fun addComponents(listComponent: List<BaseShopHeaderComponentUiModel>){
        addElement(listComponent)
        notifyDataSetChanged()
    }

}