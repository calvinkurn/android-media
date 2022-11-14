package com.tokopedia.product.addedit.common.util

import com.tokopedia.remoteconfig.RemoteConfigInstance

object RollanceUtil {
    const val MEDIA_PICKER_AB_TEST_KEY = "android_revampeditor"
    private const val IMAGE_PICKER_AB_TEST_VALUE_ENABLED = "android_revampeditor"

    fun getImagePickerRollence(): Boolean{
        return RemoteConfigInstance.getInstance().abTestPlatform
            .getString(MEDIA_PICKER_AB_TEST_KEY, "")
            .equals(IMAGE_PICKER_AB_TEST_VALUE_ENABLED, ignoreCase = false)
    }

}
