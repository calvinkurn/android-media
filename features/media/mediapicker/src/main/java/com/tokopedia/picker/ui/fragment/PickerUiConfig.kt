package com.tokopedia.picker.ui.fragment

import com.tokopedia.picker.ui.common.PickerFragmentType
import com.tokopedia.picker.ui.common.PickerModeType
import com.tokopedia.picker.ui.common.PickerPageType
import com.tokopedia.picker.ui.common.PickerSelectionType

object PickerUiConfig {

    @PickerPageType
    var paramPage = PickerPageType.COMMON

    @PickerModeType
    var paramMode = PickerModeType.COMMON

    @PickerSelectionType
    var paramType = PickerSelectionType.MULTIPLE

    fun getStatePage(): Int {
        return when (paramPage) {
            PickerPageType.GALLERY -> PickerFragmentType.GALLERY
            else -> PickerFragmentType.CAMERA
        }
    }

}