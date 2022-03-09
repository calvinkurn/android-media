package com.tokopedia.media.picker.ui

import android.net.Uri
import com.tokopedia.applink.ApplinkConst.MediaPicker
import com.tokopedia.media.common.PickerParam
import com.tokopedia.media.common.types.PickerModeType
import com.tokopedia.media.common.types.PickerPageType
import com.tokopedia.media.common.types.PickerSelectionType

object PickerUiConfig {

    @PickerPageType
    var pageType = PickerPageType.COMMON
        private set

    @PickerModeType
    var modeType = PickerModeType.COMMON
        private set

    @PickerSelectionType
    var selectionMode = PickerSelectionType.MULTIPLE
        private set

    var pickerParam: PickerParam? = null
        private set

    fun isSingleSelectionType()
        = selectionMode == PickerSelectionType.SINGLE

    fun isPhotoModeOnly()
        = modeType == PickerModeType.IMAGE_ONLY

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
            MediaPicker.VALUE_PAGE_CAMERA -> PickerPageType.CAMERA
            MediaPicker.VALUE_PAGE_GALLERY -> PickerPageType.GALLERY
            else -> PickerPageType.COMMON
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
            MediaPicker.VALUE_MODE_IMAGE -> PickerModeType.IMAGE_ONLY
            MediaPicker.VALUE_MODE_VIDEO -> PickerModeType.VIDEO_ONLY
            else -> PickerModeType.COMMON
        }
    }

    /**
     * querySelection is to specify the type of selection mode of picker.
     * you can set the selection type as [PickerSelectionType.SINGLE] or
     * as [PickerSelectionType.MULTIPLE].
     *
     * the data comes from:
     * tokopedia://media-picker?type=...
     */
    fun setupQuerySelectionType(data: Uri) {
        selectionMode = when (data.getQueryParameter(MediaPicker.PARAM_SELECTION)) {
            MediaPicker.VALUE_TYPE_SINGLE -> PickerSelectionType.SINGLE
            else -> PickerSelectionType.MULTIPLE // default
        }
    }

}