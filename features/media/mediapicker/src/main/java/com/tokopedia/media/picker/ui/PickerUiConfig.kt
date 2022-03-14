package com.tokopedia.media.picker.ui

import android.net.Uri
import com.tokopedia.applink.ApplinkConst.MediaPicker
import com.tokopedia.picker.common.types.ModeType
import com.tokopedia.picker.common.types.PageType

object PickerUiConfig {

    @PageType
    var pageType = PageType.COMMON
        private set

    @ModeType
    var modeType = ModeType.COMMON
        private set

    var isMultipleSelectionMode = true
        private set

    var startPageIndex = 0
        private set

    fun isPhotoModeOnly()
        = modeType == ModeType.IMAGE_ONLY

    /**
     * queryPage is to specify the desired page type.
     * mediapicker has options to set for:
     * 1. camera page only
     * 2. gallery page only
     * 3. camera & gallery
     *
     * the data comes from:
     * tokopedia://media-picker?page=...
     */
    fun setupQueryPage(data: Uri) {
        pageType = when(data.getQueryParameter(MediaPicker.PARAM_PAGE)) {
            MediaPicker.VALUE_PAGE_CAMERA -> PageType.CAMERA
            MediaPicker.VALUE_PAGE_GALLERY -> PageType.GALLERY
            else -> PageType.COMMON
        }
    }

    /**
     * queryMode is to determine the type of media should be display.
     * this also have ability to set the camera mode (camera only, video only, or both).
     * we have 3 options for the type of media, such as:
     * 1. image only
     * 2. video only
     * 3. image & video
     *
     * the data comes from:
     * tokopedia://media-picker?mode=...
     */
    fun setupQueryMode(data: Uri) {
        modeType = when(data.getQueryParameter(MediaPicker.PARAM_MODE)) {
            MediaPicker.VALUE_MODE_IMAGE -> ModeType.IMAGE_ONLY
            MediaPicker.VALUE_MODE_VIDEO -> ModeType.VIDEO_ONLY
            else -> ModeType.COMMON
        }
    }

    /**
     * querySelection is to specify the type of selection mode of picker.
     * you can set the selection type as [SINGLE] or [MULTIPLE].
     *
     * the data comes from:
     * tokopedia://media-picker?type=...
     */
    fun setupQuerySelectionType(data: Uri) {
        isMultipleSelectionMode = when (data.getQueryParameter(MediaPicker.PARAM_SELECTION)) {
            MediaPicker.VALUE_TYPE_SINGLE -> false
            else -> true
        }
    }

    fun setupQueryLandingPageIndex(data: Uri) {
        val value = data.getQueryParameter(MediaPicker.PARAM_LANDING_PAGE)?: "0"
        startPageIndex = value.toInt()
    }

}