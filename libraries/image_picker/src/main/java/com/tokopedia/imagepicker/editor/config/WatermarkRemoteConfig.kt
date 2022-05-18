package com.tokopedia.imagepicker.editor.config

import com.tokopedia.remoteconfig.RemoteConfig

object WatermarkRemoteConfig {

    private const val KEY = "media_watermark_editor_tool";

    @JvmStatic
    fun get(config: RemoteConfig): Boolean {
        return config.getBoolean(KEY)
    }

}