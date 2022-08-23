package com.tokopedia.shopdiscount.manage_discount.presentation.adapter

import com.tokopedia.shopdiscount.manage_discount.data.uimodel.ShopDiscountManageDiscountGlobalErrorUiModel
import com.tokopedia.shopdiscount.manage_discount.data.uimodel.ShopDiscountSetupProductShimmeringUiModel
import com.tokopedia.shopdiscount.manage_discount.data.uimodel.ShopDiscountSetupProductUiModel

interface ShopDiscountManageDiscountTypeFactory {
    fun type(uiModel: ShopDiscountSetupProductUiModel.SetupProductData): Int
    fun type(uiModel: ShopDiscountSetupProductShimmeringUiModel): Int
    fun type(shopDiscountManageDiscountGlobalErrorUiModel: ShopDiscountManageDiscountGlobalErrorUiModel): Int
}