package com.tokopedia.shop.common.view.interfaces

import androidx.lifecycle.LiveData
import com.tokopedia.shop.common.view.model.ShopPageFabConfig

interface HasSharedViewModel {
    fun getSharedViewModel(): Any
}

interface ISharedViewModel {
    val feedTabClearCache: LiveData<Boolean>
    fun showSellerMigrationBottomSheet()
    fun hideSellerMigrationBottomSheet()
    fun setupShopPageFab(fabConfig: ShopPageFabConfig)
    fun showShopPageFab()
    fun hideShopPageFab()
}