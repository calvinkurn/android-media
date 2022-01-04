package com.tokopedia.imagepicker.editor.config

import com.tokopedia.remoteconfig.RemoteConfigInstance

object AbTestRemoveBackground {

    private const val KEY = "remove_bg_android"

    @JvmStatic
    fun get(): Boolean {
        val abPlatform = RemoteConfigInstance
            .getInstance()
            .abTestPlatform
            .getString(KEY, "")

        return abPlatform.isNotEmpty()
    }

}