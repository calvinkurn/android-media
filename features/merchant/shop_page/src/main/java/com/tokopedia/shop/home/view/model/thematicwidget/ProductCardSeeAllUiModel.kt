package com.tokopedia.shop.home.view.model.thematicwidget

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.shop.home.view.adapter.viewholder.thematicwidget.typefactory.ShopHomeThematicWidgetTypeFactory

data class ProductCardSeeAllUiModel (val appLink: String) : Visitable<ShopHomeThematicWidgetTypeFactory> {
    override fun type(typeFactory: ShopHomeThematicWidgetTypeFactory): Int {
        return typeFactory.type(this)
    }
}
