package com.tokopedia.imagepicker_insta.fragment

import androidx.annotation.IntDef
import com.tokopedia.imagepicker_insta.models.Asset
import com.tokopedia.unifycomponents.Toaster

interface ImagePickerFragmentContract {
    fun handleOnCameraIconTap()
    fun showToast(message: String, toasterType: Int = Toaster.TYPE_NORMAL)
    fun isMultiSelectEnable(): Boolean
    fun getAssetInPreview(): Asset?
    fun showErrorToast(@AdapterErrorType type:Int)
}

@kotlin.annotation.Retention(AnnotationRetention.SOURCE)
@IntDef(AdapterErrorType.MULTISELECT,AdapterErrorType.VIDEO_DURATION)
annotation class AdapterErrorType {
    companion object {
        const val MULTISELECT = 0
        const val VIDEO_DURATION = 1
    }
}