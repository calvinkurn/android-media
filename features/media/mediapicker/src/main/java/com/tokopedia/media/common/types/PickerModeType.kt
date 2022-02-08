package com.tokopedia.media.common.types

import androidx.annotation.IntDef

@Retention(AnnotationRetention.SOURCE)
@Target(
    AnnotationTarget.PROPERTY,
    AnnotationTarget.FIELD,
    AnnotationTarget.VALUE_PARAMETER
)
@IntDef(value = [
    PickerModeType.COMMON,
    PickerModeType.IMAGE_ONLY,
    PickerModeType.VIDEO_ONLY,
])
annotation class PickerModeType {
    companion object {
        const val COMMON = 0
        const val IMAGE_ONLY = 1
        const val VIDEO_ONLY = 2
    }
}