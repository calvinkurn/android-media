package com.tokopedia.seller.menu.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.seller.menu.common.view.uimodel.shopinfo.SettingShopInfoUiModel
import com.tokopedia.seller.menu.presentation.adapter.SellerMenuTypeFactory

data class ShopInfoUiModel(
    val shopInfo: SettingShopInfoUiModel,
    val shopScore: Long = 0,
    val shopAge: Long = 0
) :
    Visitable<SellerMenuTypeFactory> {
    override fun type(typeFactory: SellerMenuTypeFactory): Int {
        return typeFactory.type(this)
    }

}