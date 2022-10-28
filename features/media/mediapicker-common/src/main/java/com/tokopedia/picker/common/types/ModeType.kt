package com.tokopedia.picker.common.types

import androidx.annotation.IntDef

@Retention(AnnotationRetention.SOURCE)
@Target(
    AnnotationTarget.PROPERTY,
    AnnotationTarget.FIELD,
    AnnotationTarget.VALUE_PARAMETER
)
@IntDef(value = [
    ModeType.COMMON,
    ModeType.IMAGE_ONLY,
    ModeType.VIDEO_ONLY,
])
annotation class ModeType {
    companion object {
        const val COMMON = 0
        const val IMAGE_ONLY = 1
        const val VIDEO_ONLY = 2
    }
}