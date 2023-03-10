package com.tokopedia.product.addedit.common.util

import android.content.Context
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl

object RemoteConfig {
    private const val ENABLE_NEW_MEDIA_PICKER_AEP = "android_enable_new_media_picker"
    fun getImagePickerRemoteConfig(context: Context?): Boolean {
        return context?.let {
            FirebaseRemoteConfigImpl(context).getBoolean(ENABLE_NEW_MEDIA_PICKER_AEP, false)
        } ?: false
    }
}
