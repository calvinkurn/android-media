package com.tokopedia.picker.common.types

import androidx.annotation.IntDef

@Retention(AnnotationRetention.SOURCE)
@Target(
    AnnotationTarget.PROPERTY,
    AnnotationTarget.FIELD,
    AnnotationTarget.VALUE_PARAMETER
)
@IntDef(value = [
    EditorToolType.NONE,
    EditorToolType.BRIGHTNESS,
    EditorToolType.CONTRAST,
    EditorToolType.CROP,
    EditorToolType.ROTATE,
    EditorToolType.REMOVE_BACKGROUND,
    EditorToolType.WATERMARK,
])
annotation class EditorToolType {
    companion object {
        const val NONE = -1
        const val BRIGHTNESS = 0
        const val CONTRAST = 1
        const val CROP = 2
        const val ROTATE = 3
        const val REMOVE_BACKGROUND = 4
        const val WATERMARK = 5
    }
}