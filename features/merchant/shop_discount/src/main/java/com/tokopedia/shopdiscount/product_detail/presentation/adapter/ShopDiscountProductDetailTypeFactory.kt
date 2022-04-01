package com.tokopedia.shopdiscount.product_detail.presentation.adapter

import com.tokopedia.shopdiscount.product_detail.data.uimodel.ShopDiscountProductDetailListGlobalErrorUiModel
import com.tokopedia.shopdiscount.product_detail.data.uimodel.ShopDiscountProductDetailShimmeringUiModel
import com.tokopedia.shopdiscount.product_detail.data.uimodel.ShopDiscountProductDetailUiModel

interface ShopDiscountProductDetailTypeFactory {
    fun type(uiModel: ShopDiscountProductDetailUiModel): Int
    fun type(shopDiscountProductDetailShimmeringUiModel: ShopDiscountProductDetailShimmeringUiModel): Int
    fun type(shopDiscountProductDetailListGlobalErrorUiModel: ShopDiscountProductDetailListGlobalErrorUiModel): Int
}