package com.tokopedia.media.editor.data

import com.tokopedia.remoteconfig.RemoteConfigInstance
import javax.inject.Inject

interface FeatureToggleManager {
    fun isAddLogoEnable(): Boolean
    fun isAddTextEnable(): Boolean
}

class FeatureToggleManagerImpl @Inject constructor() : FeatureToggleManager {
    override fun isAddLogoEnable(): Boolean {
        return RemoteConfigInstance
            .getInstance()
            .abTestPlatform
            .getString(EDITOR_ADD_LOGO_TOOL) == EDITOR_ADD_LOGO_TOOL
    }

    override fun isAddTextEnable(): Boolean {
        return RemoteConfigInstance
            .getInstance()
            .abTestPlatform
            .getString(EDITOR_ADD_TEXT_TOOL) == EDITOR_ADD_TEXT_TOOL
    }

    companion object {
        const val EDITOR_ADD_LOGO_TOOL = "android_addlogo"
        const val EDITOR_ADD_TEXT_TOOL = "android_addtext"
    }
}
