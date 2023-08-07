package com.tokopedia.picker.common.types

import androidx.annotation.IntDef

@Retention(AnnotationRetention.SOURCE)
@Target(
    AnnotationTarget.PROPERTY,
    AnnotationTarget.FIELD,
    AnnotationTarget.VALUE_PARAMETER
)
@IntDef(value = [
    // default
    ToolType.NONE,

    // image and video
    ToolType.TEXT,

    // image
    ToolType.PLACEMENT,

    // video
    ToolType.AUDIO_MUTE,
    ToolType.TRIM,
])
annotation class UniversalEditorToolType {
    companion object {
        const val NONE = -1
        const val TEXT = 1
        const val PLACEMENT = 2
        const val AUDIO_MUTE = 3
        const val TRIM = 4
    }
}

typealias ToolType = UniversalEditorToolType
