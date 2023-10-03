package com.tokopedia.media.picker.data

import com.tokopedia.picker.common.PICKER_TO_EDITOR_ROLLENCE
import com.tokopedia.picker.common.PICKER_TO_IMMERSIVE_EDITOR_ROLLENCE
import com.tokopedia.remoteconfig.RemoteConfigInstance
import javax.inject.Inject

interface FeatureToggleManager {
    fun isEditorEnabled(): Boolean
    fun isImmersiveEditorEnable(): Boolean
}

class FeatureToggleManagerImpl @Inject constructor() : FeatureToggleManager {
    override fun isEditorEnabled(): Boolean {
        return RemoteConfigInstance
            .getInstance()
            .abTestPlatform
            .getString(PICKER_TO_EDITOR_ROLLENCE) == PICKER_TO_EDITOR_ROLLENCE
    }

    override fun isImmersiveEditorEnable(): Boolean {
        return RemoteConfigInstance
            .getInstance()
            .abTestPlatform
            .getString(PICKER_TO_IMMERSIVE_EDITOR_ROLLENCE)
            .isNotEmpty()
    }
}
