package com.tokopedia.shopdiscount.product_detail.data.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.shopdiscount.product_detail.presentation.adapter.ShopDiscountProductDetailTypeFactory

data class ShopDiscountProductDetailListGlobalErrorUiModel(
    val throwable: Throwable
) : Visitable<ShopDiscountProductDetailTypeFactory> {
    override fun type(typeFactory: ShopDiscountProductDetailTypeFactory): Int {
        return typeFactory.type(this)
    }
}