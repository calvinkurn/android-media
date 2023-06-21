package com.tokopedia.shopdiscount.manage_discount.data.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.shopdiscount.manage_discount.presentation.adapter.ShopDiscountManageDiscountTypeFactory

data class ShopDiscountManageDiscountGlobalErrorUiModel(
    val throwable: Throwable
) : Visitable<ShopDiscountManageDiscountTypeFactory> {
    override fun type(typeFactory: ShopDiscountManageDiscountTypeFactory): Int {
        return typeFactory.type(this)
    }
}
