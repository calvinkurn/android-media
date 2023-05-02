package com.tokopedia.media.editor.analytics

import com.tokopedia.media.editor.R
import com.tokopedia.media.editor.data.repository.WatermarkType
import com.tokopedia.media.editor.ui.component.AddLogoToolUiComponent
import com.tokopedia.media.editor.ui.uimodel.EditorDetailUiModel
import com.tokopedia.picker.common.types.EditorToolType

// for analytics purpose
fun getToolEditorText(editorToolType: Int): Int {
    return when (editorToolType) {
        EditorToolType.BRIGHTNESS -> R.string.editor_tool_brightness
        EditorToolType.CONTRAST -> R.string.editor_tool_contrast
        EditorToolType.WATERMARK -> R.string.editor_tool_watermark
        EditorToolType.ROTATE -> R.string.editor_tool_rotate
        EditorToolType.CROP -> R.string.editor_tool_crop
        EditorToolType.ADD_LOGO -> R.string.editor_tool_add_logo
        else -> R.string.editor_tool_remove_background
    }
}

fun cropRatioToText(ratio: Pair<Int, Int>): String {
    return if (ratio.first != 0) {
        "${ratio.first}:${ratio.second}"
    } else {
        ""
    }
}

fun removeBackgroundToText(removeBackgroundType: Int?): String {
    return when (removeBackgroundType) {
        EditorDetailUiModel.REMOVE_BG_TYPE_GRAY -> REMOVE_BG_TYPE_GREY
        EditorDetailUiModel.REMOVE_BG_TYPE_WHITE -> REMOVE_BG_TYPE_WHITE
        else -> REMOVE_BG_TYPE_ORI
    }
}

fun watermarkToText(watermarkType: Int?): String {
    return when (watermarkType) {
        WatermarkType.Center.value -> WATERMARK_TYPE_CENTER
        WatermarkType.Diagonal.value -> WATERMARK_TYPE_DIAGONAL
        else -> ""
    }
}

fun addLogoToText(logoState: Int?): String {
    return when (logoState) {
        AddLogoToolUiComponent.LOGO_UPLOAD -> ADD_LOGO_TYPE_CUSTOM
        AddLogoToolUiComponent.LOGO_SHOP -> ADD_LOGO_TYPE_PROFILE
        else -> ""
    }
}
