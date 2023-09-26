package com.tokopedia.home.beranda.presentation.view.helper

import com.tokopedia.home.beranda.di.HomeScope
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import javax.inject.Inject

/**
 * Created by frenzel on 09/05/22.
 */
@HomeScope
class HomeRemoteConfigController @Inject constructor (
    private val remoteConfig: RemoteConfig,
) {
    var atfRemoteConfig: Boolean = true

    fun fetchHomeRemoteConfig() {
        atfRemoteConfig = try {
            remoteConfig.getBoolean(RemoteConfigKey.HOME_ATF_REFACTORING, true)
        } catch (_: Exception) {
            true
        }
    }

    fun isUsingNewAtf(): Boolean {
        return atfRemoteConfig
    }
}
