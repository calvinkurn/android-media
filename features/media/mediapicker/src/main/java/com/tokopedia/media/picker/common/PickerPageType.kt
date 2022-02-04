package com.tokopedia.media.picker.common

import androidx.annotation.IntDef

@Retention(AnnotationRetention.SOURCE)
@Target(
    AnnotationTarget.PROPERTY,
    AnnotationTarget.FIELD,
    AnnotationTarget.VALUE_PARAMETER
)
@IntDef(value = [
    PickerPageType.COMMON,
    PickerPageType.CAMERA,
    PickerPageType.GALLERY,
])
annotation class PickerPageType {
    companion object {
        const val COMMON = 0
        const val CAMERA = 1
        const val GALLERY = 2
    }
}