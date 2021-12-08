package com.tokopedia.picker.ui.common

import androidx.annotation.IntDef

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
@IntDef(value = [
    PickerFragmentType.PERMISSION,
    PickerFragmentType.CAMERA,
    PickerFragmentType.PICKER,
])
annotation class PickerFragmentType {
    companion object {
        const val PERMISSION = 0
        const val CAMERA = 1
        const val PICKER = 2
    }
}