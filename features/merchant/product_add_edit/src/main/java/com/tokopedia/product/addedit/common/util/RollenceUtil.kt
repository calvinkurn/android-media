package com.tokopedia.product.addedit.common.util

import com.tokopedia.remoteconfig.RemoteConfigInstance

object RollenceUtil {
    const val HAMPERS_AB_TEST_KEY = "picker_image_android"
    private const val IMAGE_PICKER_AB_TEST_VALUE_ENABLED = "image_picker"

    fun getImagePickerRollence(): Boolean{
        return RemoteConfigInstance.getInstance().abTestPlatform
            .getString(HAMPERS_AB_TEST_KEY, "")
            .equals(IMAGE_PICKER_AB_TEST_VALUE_ENABLED, ignoreCase = false)
    }

}
