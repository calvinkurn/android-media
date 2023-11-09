package com.tokopedia.product.detail.common

import android.content.Context
import android.content.Intent
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey

object ProductDetailPrefetch {

    const val PREFETCH_DATA_CACHE_ID = "prefetch_data_cache_id"

    /**
     * Include CacheId into Product Detail Intent
     */
    fun process(
        context: Context,
        intent: Intent,
        data: Data
    ) {
        val remoteConfig = FirebaseRemoteConfigImpl(context)
        val enablePrefetch = remoteConfig.getBoolean(RemoteConfigKey.ENABLE_PDP_PREFETCH, false)
        if (!enablePrefetch) return

        val cacheManager = SaveInstanceCacheManager(context, true)
        cacheManager.put(Data::class.java.simpleName, data)

        val cacheId = cacheManager.id ?: ""
        intent.putExtra(PREFETCH_DATA_CACHE_ID, cacheId)
    }

    data class Data(
        val image: String,
        val name: String,
        val price: Double,
        val slashPrice: String,
        val discount: Int,
        val freeShippingLogo: String,
        val rating: String,
        val integrity: String
    )
}
