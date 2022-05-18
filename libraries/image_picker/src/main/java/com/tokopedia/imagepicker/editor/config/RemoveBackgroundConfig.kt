package com.tokopedia.imagepicker.editor.config

import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigInstance

object RemoveBackgroundConfig {

    private const val ROLLENCE_KEY = "remove_bg_android"
    private const val REMOTE_CONFIG_KEY = "media_removebg_editor_tool"

    @JvmStatic
    fun abTest(): Boolean {
        val abPlatform = RemoteConfigInstance
            .getInstance()
            .abTestPlatform
            .getString(ROLLENCE_KEY, "")

        return abPlatform.isNotEmpty()
    }

    @JvmStatic
    fun remoteConfig(config: RemoteConfig): Boolean {
        return config.getBoolean(REMOTE_CONFIG_KEY)
    }

}