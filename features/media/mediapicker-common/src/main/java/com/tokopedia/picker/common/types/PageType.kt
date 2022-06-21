package com.tokopedia.picker.common.types

import androidx.annotation.IntDef

@Retention(AnnotationRetention.SOURCE)
@Target(
    AnnotationTarget.PROPERTY,
    AnnotationTarget.FIELD,
    AnnotationTarget.VALUE_PARAMETER
)
@IntDef(value = [
    PageType.COMMON,
    PageType.CAMERA,
    PageType.GALLERY,
])
annotation class PageType {
    companion object {
        const val COMMON = 0
        const val CAMERA = 1
        const val GALLERY = 2
    }
}