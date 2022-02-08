package com.tokopedia.media.common.types

import androidx.annotation.IntDef

@Retention(AnnotationRetention.SOURCE)
@Target(
    AnnotationTarget.PROPERTY,
    AnnotationTarget.FIELD,
    AnnotationTarget.VALUE_PARAMETER
)
@IntDef(value = [
    PickerSelectionType.SINGLE,
    PickerSelectionType.MULTIPLE,
])
annotation class PickerSelectionType {
    companion object {
        const val SINGLE = 0
        const val MULTIPLE = 1
    }
}