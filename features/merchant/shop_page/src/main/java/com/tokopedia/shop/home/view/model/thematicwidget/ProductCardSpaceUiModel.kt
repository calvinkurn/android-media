package com.tokopedia.shop.home.view.model.thematicwidget

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.shop.home.view.adapter.viewholder.thematicwidget.typefactory.ShopHomeThematicWidgetTypeFactory

class ProductCardSpaceUiModel : Visitable<ShopHomeThematicWidgetTypeFactory> {
    override fun type(typeFactory: ShopHomeThematicWidgetTypeFactory): Int {
        return typeFactory.type(this)
    }
}
