package com.tokopedia.content.common.util.remoteconfig

import com.tokopedia.remoteconfig.RemoteConfig
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on January 31, 2023
 */
class PlayShortsEntryPointRemoteConfig @Inject constructor(
    private val remoteConfig: RemoteConfig,
) {

    fun isShowEntryPoint(): Boolean {
        return false
//        return remoteConfig.getBoolean(PLAY_SHORTS_ENTRY_POINT_REMOTE_CONFIG, true)
    }

    companion object {
        private const val PLAY_SHORTS_ENTRY_POINT_REMOTE_CONFIG = "android_main_app_enable_play_shorts"
    }
}
