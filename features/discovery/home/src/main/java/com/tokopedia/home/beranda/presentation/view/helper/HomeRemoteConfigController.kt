package com.tokopedia.home.beranda.presentation.view.helper

import android.content.Context
import com.tokopedia.device.info.DeviceScreenInfo
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
    var atfRemoteConfig: Boolean = false

    fun fetchHomeRemoteConfig(context: Context) {
        atfRemoteConfig = try {
            remoteConfig.getBoolean(RemoteConfigKey.HOME_ATF_REFACTORING, true)
        } catch (_: Exception) {
            false
        }
    }

    fun isUsingNewAtf(): Boolean {
        return atfRemoteConfig
    }
}
