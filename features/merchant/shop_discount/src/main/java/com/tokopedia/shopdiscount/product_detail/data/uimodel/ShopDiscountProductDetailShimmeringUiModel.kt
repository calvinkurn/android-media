package com.tokopedia.shopdiscount.product_detail.data.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.shopdiscount.product_detail.presentation.adapter.ShopDiscountProductDetailTypeFactoryImpl

class ShopDiscountProductDetailShimmeringUiModel: Visitable<ShopDiscountProductDetailTypeFactoryImpl> {
    override fun type(typeFactory: ShopDiscountProductDetailTypeFactoryImpl): Int {
        return typeFactory.type(this)
    }
}