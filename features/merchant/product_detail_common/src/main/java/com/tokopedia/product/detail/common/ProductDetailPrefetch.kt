package com.tokopedia.product.detail.common

import android.content.Context
import android.content.Intent
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.remoteconfig.RollenceKey

object ProductDetailPrefetch {

    const val PREFETCH_DATA_CACHE_ID = "prefetchId"

    fun process(
        context: Context?,
        appLink: String,
        data: Data
    ): String {
        if (context == null) return appLink
        val isValid = validateAppLink(appLink)
        if (!isValid) return appLink

        val remoteConfig = checkRemoteConfig(context)
        val rollence = checkRollence()
        if (!rollence || !remoteConfig) return appLink

        val cacheManager = SaveInstanceCacheManager(context, true)
        cacheManager.put(Data::class.java.simpleName, data)

        val cacheId = cacheManager.id ?: return appLink

        val connector = if (appLink.contains("?")) "&" else "?"
        val extraParam = "$connector$PREFETCH_DATA_CACHE_ID=$cacheId"
        return appLink + extraParam
    }

    /**
     * Include CacheId into Product Detail Intent
     */
    fun process(
        context: Context,
        intent: Intent,
        data: Data
    ) {
        val remoteConfig = checkRemoteConfig(context)
        val rollence = checkRollence()
        if (!rollence || !remoteConfig) return

        val cacheManager = SaveInstanceCacheManager(context, true)
        cacheManager.put(Data::class.java.simpleName, data)

        val cacheId = cacheManager.id ?: ""
        intent.putExtra(PREFETCH_DATA_CACHE_ID, cacheId)
    }

    private fun checkRemoteConfig(context: Context): Boolean {
        val remoteConfig = FirebaseRemoteConfigImpl(context)
        return remoteConfig.getBoolean(RemoteConfigKey.ENABLE_PDP_PREFETCH, false)
    }

    private fun checkRollence(): Boolean {
        val platform = RemoteConfigInstance.getInstance().abTestPlatform
        val result = platform.getString(RollenceKey.PDP_PREFETCH, RollenceKey.PDP_PREFETCH_DISABLE)
        return result == RollenceKey.PDP_PREFETCH_ENABLE
    }

    fun validateAppLink(appLink: String): Boolean {
        val prefix = "tokopedia://product"
        return appLink.startsWith(prefix)
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
