package com.tokopedia.seller.menu.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.seller.menu.common.view.typefactory.OtherMenuAdapterTypeFactory
import com.tokopedia.seller.menu.common.view.uimodel.base.SettingUiModel
import com.tokopedia.seller.menu.common.view.uimodel.shopinfo.SettingShopInfoUiModel
import com.tokopedia.seller.menu.common.view.uimodel.shopinfo.ShopInfoErrorUiModel
import com.tokopedia.seller.menu.common.view.uimodel.shopinfo.ShopInfoLoadingUiModel
import com.tokopedia.seller.menu.common.view.uimodel.shopinfo.ShopInfoUiModel

class SellerMenuAdapter(
    factory: OtherMenuAdapterTypeFactory
): BaseListAdapter<SettingUiModel, OtherMenuAdapterTypeFactory>(factory) {

    companion object {
        private const val SHOP_INFO_INDEX = 0
    }

    fun showShopInfo(shopInfo: SettingShopInfoUiModel) {
        removeShopInfoItem()

        val shopInfoUiModel = ShopInfoUiModel(shopInfo)
        addElement(SHOP_INFO_INDEX, shopInfoUiModel)

        notifyItemChanged(SHOP_INFO_INDEX)
    }

    fun showShopInfoLoading() {
        removeShopInfoItem()

        addElement(SHOP_INFO_INDEX, ShopInfoLoadingUiModel)
        notifyItemChanged(SHOP_INFO_INDEX)
    }

    fun showShopInfoError() {
        removeShopInfoItem()

        addElement(SHOP_INFO_INDEX, ShopInfoErrorUiModel)
        notifyItemChanged(SHOP_INFO_INDEX)
    }

    private fun removeShopInfoItem() {
        visitables.removeAt(SHOP_INFO_INDEX)
        notifyItemRemoved(SHOP_INFO_INDEX)
    }
}