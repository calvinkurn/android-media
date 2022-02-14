package com.tokopedia.shop_widget.common.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.shop_widget.common.typefactory.ProductCardTypeFactory

class ProductCardSpaceUiModel : Visitable<ProductCardTypeFactory> {
    override fun type(typeFactory: ProductCardTypeFactory): Int {
        return typeFactory.type(this)
    }
}