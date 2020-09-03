package com.tokopedia.seller.menu.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.seller.menu.common.view.typefactory.OtherMenuAdapterTypeFactory
import com.tokopedia.seller.menu.common.view.uimodel.ShopProductUiModel
import com.tokopedia.seller.menu.common.view.uimodel.base.SettingUiModel
import com.tokopedia.seller.menu.common.view.uimodel.shopinfo.SettingShopInfoUiModel
import com.tokopedia.seller.menu.common.view.uimodel.shopinfo.ShopInfoErrorUiModel
import com.tokopedia.seller.menu.common.view.uimodel.shopinfo.ShopInfoLoadingUiModel
import com.tokopedia.seller.menu.common.view.uimodel.shopinfo.ShopInfoUiModel

class SellerMenuAdapter(
    factory: OtherMenuAdapterTypeFactory
) : BaseListAdapter<SettingUiModel, OtherMenuAdapterTypeFactory>(factory) {

    fun showShopInfo(shopInfo: SettingShopInfoUiModel) {
        findShopInfoIndex()?.let { index ->
            val shopInfoUiModel = ShopInfoUiModel(shopInfo)
            updateItemAt(index, shopInfoUiModel)
        }
    }

    fun showShopInfoLoading() {
        findShopInfoIndex()?.let { index ->
            updateItemAt(index, ShopInfoLoadingUiModel)
        }
    }

    fun showShopInfoError() {
        findShopInfoIndex()?.let { index ->
            updateItemAt(index, ShopInfoErrorUiModel)
        }
    }

    fun showProductSection(productUiModel: ShopProductUiModel) {
        findIndex { it is ShopProductUiModel }?.let { index ->
            updateItemAt(index, productUiModel)
        }
    }

    private fun updateItemAt(index: Int, item: SettingUiModel) {
        visitables.removeAt(index)
        notifyItemRemoved(index)

        addElement(index, item)
        notifyItemChanged(index)
    }

    private fun findShopInfoIndex(): Int? {
        return findIndex {
            it is ShopInfoUiModel ||
            it is ShopInfoLoadingUiModel ||
            it is ShopInfoErrorUiModel
        }
    }

    private fun findIndex(predicate: (SettingUiModel) -> Boolean): Int? {
        val item = data.firstOrNull { predicate.invoke(it) }
        return item?.let { data.indexOf(it) }
    }
}