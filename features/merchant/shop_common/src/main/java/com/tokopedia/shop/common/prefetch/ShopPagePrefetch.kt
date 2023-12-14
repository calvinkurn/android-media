package com.tokopedia.shop.common.prefetch

import android.content.Context
import android.content.Intent
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RollenceKey
import com.tokopedia.shop.common.domain.entity.ShopPrefetchData

class ShopPagePrefetch {
    companion object {
        const val BUNDLE_KEY_PREFETCH_CACHE_ID = "shop_prefetch_cache_id"
    }

    fun redirectToShopPageWithPrefetch(
        context: Context,
        prefetchData: ShopPrefetchData,
        intent: Intent
    ) {
        if (!isPrefetchEnabledOnRemoteConfig(context) || !isPrefetchEnabledOnRollence()) return

        val cacheManager = SaveInstanceCacheManager(context = context, generateObjectId = true)

        cacheManager.put(
            customId = ShopPrefetchData::class.java.simpleName,
            objectToPut = prefetchData
        )

        val cacheId = cacheManager.id
        intent.putExtra(BUNDLE_KEY_PREFETCH_CACHE_ID, cacheId)
    }

    private fun isPrefetchEnabledOnRemoteConfig(context: Context): Boolean {
        val remoteConfig = FirebaseRemoteConfigImpl(context)
        // return remoteConfig.getBoolean(RemoteConfigKey.ENABLE_SHOP_PAGE_PREFETCH, false)
        return true
    }

    private fun isPrefetchEnabledOnRollence(): Boolean {
        val abTestPlatform = RemoteConfigInstance.getInstance().abTestPlatform
        val result = abTestPlatform.getString(RollenceKey.AB_TEST_SHOP_PREFETCH, RollenceKey.AB_TEST_SHOP_PREFETCH_DISABLED)
        // return result == RollenceKey.AB_TEST_SHOP_PREFETCH_ENABLED
        return true
    }
}
