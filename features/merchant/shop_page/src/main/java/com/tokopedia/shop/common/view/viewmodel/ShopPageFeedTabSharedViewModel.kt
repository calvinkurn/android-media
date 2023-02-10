package com.tokopedia.shop.common.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.shop.common.view.interfaces.ISharedViewModel
import com.tokopedia.shop.common.view.model.ShopPageFabConfig

/**
 * Shared ViewModel to share live data between NewShopPageFragment.kt and FeedShopFragment.kt
 */
class ShopPageFeedTabSharedViewModel : ViewModel(), ISharedViewModel {

    companion object {
        const val FAB_ACTION_SETUP = 0
        const val FAB_ACTION_SHOW = 1
        const val FAB_ACTION_HIDE = -1
    }

    var shopPageFabConfig = ShopPageFabConfig()

    override val feedTabClearCache: LiveData<Boolean>
        get() = _feedTabClearCache
    private val _feedTabClearCache = MutableLiveData<Boolean>()

    val sellerMigrationBottomSheet: LiveData<Boolean>
        get() = _sellerMigrationBottomSheet
    private val _sellerMigrationBottomSheet = MutableLiveData<Boolean>()

    val shopPageFab: LiveData<Int>
        get() = _shopPageFab
    private val _shopPageFab = MutableLiveData<Int>()

    override fun showSellerMigrationBottomSheet() {
        _sellerMigrationBottomSheet.value = true
    }

    override fun hideSellerMigrationBottomSheet() {
        _sellerMigrationBottomSheet.value = false
    }

    override fun setupShopPageFab(fabConfig: ShopPageFabConfig) {
        shopPageFabConfig = fabConfig
        _shopPageFab.value = FAB_ACTION_SETUP
    }

    override fun showShopPageFab() {
        _shopPageFab.value = FAB_ACTION_SHOW
    }

    override fun hideShopPageFab() {
        _shopPageFab.value = FAB_ACTION_HIDE
    }

    fun clearCache() {
        _feedTabClearCache.value = true
    }
}
