package com.tokopedia.product.addedit.common.util

import com.tokopedia.remoteconfig.RemoteConfigInstance

object Rollence {
    private const val Rollence_KEY = "android_editorfix2"
    private const val Rollence_VALUE = "android_editorfix2"

    fun getImagePickerRollence(): Boolean{
        return RemoteConfigInstance.getInstance().abTestPlatform
            .getString(Rollence_KEY, "")
            .equals(Rollence_VALUE, ignoreCase = false)
    }

}
