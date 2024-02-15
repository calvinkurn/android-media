package com.tokopedia.home.beranda.presentation.view.helper

import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.remoteconfig.RollenceKey
import javax.inject.Inject

/**
 * Created by frenzel on 09/05/22.
 */
class HomeRemoteConfigController @Inject constructor (
    private val remoteConfig: RemoteConfig,
) {
    companion object {
        const val DEFAULT_BANNER_CACHE_EXPIRY = 15L
    }

    var atfRemoteConfig: Boolean = true
    var bannerCacheExpiryDays: Long = DEFAULT_BANNER_CACHE_EXPIRY
    private var shouldGlobalComponentRecomEnabled: Boolean = false

    fun fetchHomeRemoteConfig() {
        try {
            atfRemoteConfig = remoteConfig.getBoolean(RemoteConfigKey.HOME_ATF_REFACTORING, true)
            bannerCacheExpiryDays = remoteConfig.getLong(RemoteConfigKey.HOME_HPB_CACHE_EXPIRY, DEFAULT_BANNER_CACHE_EXPIRY)
            shouldGlobalComponentRecomEnabled = remoteConfig.getBoolean(RemoteConfigKey.HOME_GLOBAL_COMPONENT_FALLBACK, true)
        } catch (_: Exception) { }
    }

    fun isUsingNewAtf(): Boolean {
        return atfRemoteConfig && HomeRollenceController.isLoadAtfFromCache()
    }

    fun getBannerExpiryDays(): Long {
        return bannerCacheExpiryDays
    }

    fun shouldGlobalComponentRecomEnabled() = shouldGlobalComponentRecomEnabled
}
