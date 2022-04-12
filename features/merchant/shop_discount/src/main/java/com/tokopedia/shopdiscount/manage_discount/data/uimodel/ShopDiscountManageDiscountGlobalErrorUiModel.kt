package com.tokopedia.shopdiscount.manage_discount.data.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.shopdiscount.manage_discount.presentation.adapter.ShopDiscountManageDiscountTypeFactory
import com.tokopedia.shopdiscount.product_detail.presentation.adapter.ShopDiscountProductDetailTypeFactory

data class ShopDiscountManageDiscountGlobalErrorUiModel(
    val throwable: Throwable
) : Visitable<ShopDiscountManageDiscountTypeFactory> {
    override fun type(typeFactory: ShopDiscountManageDiscountTypeFactory): Int {
        return typeFactory.type(this)
    }
}