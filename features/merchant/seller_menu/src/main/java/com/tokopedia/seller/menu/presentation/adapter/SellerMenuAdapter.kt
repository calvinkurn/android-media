package com.tokopedia.seller.menu.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.seller.menu.common.view.uimodel.SellerMenuItemUiModel
import com.tokopedia.seller.menu.presentation.uimodel.ShopOrderUiModel
import com.tokopedia.seller.menu.presentation.uimodel.ShopProductUiModel
import com.tokopedia.seller.menu.common.view.uimodel.shopinfo.SettingShopInfoUiModel
import com.tokopedia.seller.menu.presentation.uimodel.ShopInfoErrorUiModel
import com.tokopedia.seller.menu.presentation.uimodel.ShopInfoLoadingUiModel
import com.tokopedia.seller.menu.presentation.uimodel.ShopInfoUiModel

class SellerMenuAdapter(
    factory: SellerMenuAdapterTypeFactory
) : BaseListAdapter<Visitable<*>, SellerMenuAdapterTypeFactory>(factory) {

    fun showShopInfo(shopInfo: SettingShopInfoUiModel, shopScore: Long, shopAge: Long) {
        findShopInfoIndex()?.let { index ->
            val shopInfoUiModel = ShopInfoUiModel(shopInfo, shopScore, shopAge)
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

    fun showProductSection(product: ShopProductUiModel) {
        findIndex { it is ShopProductUiModel }?.let { index ->
            updateItemAt(index, product)
        }
    }

    fun showOrderSection(order: ShopOrderUiModel) {
        findIndex { it is ShopOrderUiModel }?.let { index ->
            updateItemAt(index, order)
        }
    }

    fun showNotificationCounter(matcher: (Visitable<*>) -> Boolean, notificationCount: Int) {
        findItem(matcher)?.let { item ->
            updateNotificationCounter(item, notificationCount)
        }
    }

    private fun updateItemAt(index: Int, item: Visitable<*>) {
        visitables.removeAt(index)
        notifyItemRemoved(index)

        addElement(index, item)
        notifyItemChanged(index)
    }

    private fun updateNotificationCounter(item: Visitable<*>, notificationCount: Int) {
        visitables.indexOf(item).takeIf { it != -1 }?.let { index ->
            if (item is SellerMenuItemUiModel) {
                item.notificationCount = notificationCount
                notifyItemChanged(index)
            }
        }
    }

    private fun findShopInfoIndex(): Int? {
        return findIndex {
            it is ShopInfoUiModel ||
            it is ShopInfoLoadingUiModel ||
            it is ShopInfoErrorUiModel
        }
    }

    private fun findIndex(predicate: (Visitable<*>) -> Boolean): Int? {
        val item = data.firstOrNull { predicate.invoke(it) }
        return item?.let { data.indexOf(it) }
    }

    private fun findItem(predicate: (Visitable<*>) -> Boolean): Visitable<*>? {
        return data.firstOrNull { predicate(it) }
    }
}