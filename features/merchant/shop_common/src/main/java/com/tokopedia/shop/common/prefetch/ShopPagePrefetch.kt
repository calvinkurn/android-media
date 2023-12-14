package com.tokopedia.shop.common.prefetch

import android.content.Context
import android.content.Intent
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.shop.common.domain.entity.ShopPrefetchData

class ShopPagePrefetch {
    companion object {
        const val PREFETCH_CACHE_ID = "shop_prefetch_cache_id"
    }

    fun redirectToShopPageWithPrefetch(
        context: Context,
        prefetchData: ShopPrefetchData,
        intent: Intent
    ) {
        val isPrefetchEnabled = isPrefetchEnabled(context)
        if (!isPrefetchEnabled) return

        val cacheManager = SaveInstanceCacheManager(context = context, generateObjectId = true)

        cacheManager.put(
            customId = ShopPrefetchData::class.java.simpleName,
            objectToPut = prefetchData
        )

        val cacheId = cacheManager.id
        intent.putExtra(PREFETCH_CACHE_ID, cacheId)
    }

    private fun isPrefetchEnabled(context: Context): Boolean {
        val remoteConfig = FirebaseRemoteConfigImpl(context)
        // return remoteConfig.getBoolean(RemoteConfigKey.ENABLE_SHOP_PAGE_PREFETCH, false)
        return true
    }
}
