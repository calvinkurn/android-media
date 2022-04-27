package com.tokopedia.shopdiscount.manage_product_discount.presentation.adapter

import com.tokopedia.shopdiscount.manage_product_discount.data.uimodel.ShopDiscountManageProductVariantItemUiModel

interface ShopDiscountManageProductVariantDiscountTypeFactory {
    fun type(uiModel: ShopDiscountManageProductVariantItemUiModel): Int
}