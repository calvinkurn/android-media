package com.tokopedia.imagepicker.editor.config

import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigInstance

object RemoveBackgroundConfig {

    private const val REMOTE_CONFIG_KEY = "media_removebg_editor_tool"

    @JvmStatic
    fun isEnable(config: RemoteConfig): Boolean {
        return config.getBoolean(REMOTE_CONFIG_KEY)
    }

}