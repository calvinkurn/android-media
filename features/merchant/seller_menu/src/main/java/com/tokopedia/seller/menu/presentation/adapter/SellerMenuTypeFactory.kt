package com.tokopedia.seller.menu.presentation.adapter

import com.tokopedia.seller.menu.presentation.uimodel.SellerFeatureUiModel
import com.tokopedia.seller.menu.presentation.uimodel.ShopInfoErrorUiModel
import com.tokopedia.seller.menu.presentation.uimodel.ShopInfoLoadingUiModel
import com.tokopedia.seller.menu.presentation.uimodel.ShopInfoUiModel
import com.tokopedia.seller.menu.presentation.uimodel.ShopOrderUiModel
import com.tokopedia.seller.menu.presentation.uimodel.ShopProductUiModel

interface SellerMenuTypeFactory {
    fun type(shopInfoUiModel: ShopInfoUiModel): Int
    fun type(shopInfoLoadingUiModel: ShopInfoLoadingUiModel): Int
    fun type(shopInfoErrorUiModel: ShopInfoErrorUiModel): Int
    fun type(shopOrderUiModel: ShopOrderUiModel): Int
    fun type(shopProductUiModel: ShopProductUiModel): Int
    fun type(sellerFeatureUiModel: SellerFeatureUiModel): Int
}