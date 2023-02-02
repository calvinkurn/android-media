package com.tokopedia.shop.home.view.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.shop.home.view.adapter.ShopHomeAdapterTypeFactory
import com.tokopedia.shop_widget.common.util.WidgetState

data class ProductGridListPlaceholderUiModel(
    var widgetState: WidgetState = WidgetState.INIT
) : Visitable<ShopHomeAdapterTypeFactory> {
    override fun type(typeFactory: ShopHomeAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}
