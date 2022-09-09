package com.tokopedia.imagepicker_insta.common

import android.os.Bundle
import androidx.annotation.IntRange

object ImagePickerRouter {
    const val DEFAULT_MULTI_SELECT_LIMIT = 5

    fun prepareBundle(
        title: String? = null,
        toolbarIconUrl: String? = null,
        menuTitle: String? = null,
        @IntRange(from = 1L, to = DEFAULT_MULTI_SELECT_LIMIT.toLong())
        maxMultiSelectAllowed: Int = 5,
        applinkToNavigateAfterMediaCapture: String? = null,
        applinkForGalleryProceed: String? = null,
        applinkForBackNavigation: String? = null,
    ): Bundle {
        val bundle = Bundle()
        bundle.putString(BundleData.TITLE, title)
        bundle.putString(BundleData.TOOLBAR_ICON_URL, toolbarIconUrl)
        bundle.putString(BundleData.MENU_TITLE, menuTitle)
        bundle.putInt(BundleData.MAX_MULTI_SELECT_ALLOWED, Math.min(maxMultiSelectAllowed, DEFAULT_MULTI_SELECT_LIMIT))
        bundle.putString(BundleData.APPLINK_AFTER_CAMERA_CAPTURE, applinkToNavigateAfterMediaCapture)
        bundle.putString(BundleData.APPLINK_FOR_GALLERY_PROCEED, applinkForGalleryProceed)
        bundle.putString(BundleData.APPLINK_FOR_BACK_NAVIGATION, applinkForBackNavigation)
        return bundle
    }
}