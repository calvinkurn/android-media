package com.tokopedia.picker.ui.fragment

import com.tokopedia.picker.ui.common.PickerFragmentType
import com.tokopedia.picker.ui.common.PickerModeType
import com.tokopedia.picker.ui.common.PickerSelectionType

object PickerUiConfig {

    @PickerFragmentType
    var activePage = PickerFragmentType.PICKER

    @PickerModeType
    var paramMode = PickerModeType.COMMON

    @PickerSelectionType
    var paramType = PickerSelectionType.MULTIPLE

}