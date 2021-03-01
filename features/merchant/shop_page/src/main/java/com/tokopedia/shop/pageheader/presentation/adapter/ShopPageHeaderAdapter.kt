package com.tokopedia.shop.pageheader.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.shop.pageheader.presentation.adapter.typefactory.widget.ShopPageHeaderAdapterTypeFactory
import com.tokopedia.shop.pageheader.presentation.uimodel.widget.ShopHeaderWidgetUiModel
import com.tokopedia.shop.pageheader.presentation.uimodel.widget.ShopHeaderWidgetUiModel.WidgetType.SHOP_BASIC_INFO

class ShopPageHeaderAdapter(
        typeFactory: ShopPageHeaderAdapterTypeFactory
) : BaseListAdapter<Visitable<*>, ShopPageHeaderAdapterTypeFactory>(typeFactory) {

    fun setData(data: List<ShopHeaderWidgetUiModel>) {
        addElement(data)
        notifyDataSetChanged()
    }

}