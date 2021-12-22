package com.tokopedia.picker.common

import androidx.annotation.IntDef

@Retention(AnnotationRetention.SOURCE)
@Target(
    AnnotationTarget.PROPERTY,
    AnnotationTarget.FIELD,
    AnnotationTarget.VALUE_PARAMETER
)
@IntDef(value = [
    PickerFragmentType.NONE,
    PickerFragmentType.PERMISSION,
    PickerFragmentType.PICKER,
    PickerFragmentType.CAMERA,
    PickerFragmentType.GALLERY,
])
annotation class PickerFragmentType {
    companion object {
        // idle page state
        const val NONE = -1

        // runtime permission on-boarding page
        const val PERMISSION = 0

        // container of camera and gallery page
        const val PICKER = 1

        // camera and video page
        const val CAMERA = 2

        // gallery picker page
        const val GALLERY = 3
    }
}