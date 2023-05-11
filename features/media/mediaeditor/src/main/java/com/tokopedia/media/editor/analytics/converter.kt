package com.tokopedia.media.editor.analytics

import com.tokopedia.media.editor.R
import com.tokopedia.media.editor.data.repository.WatermarkType
import com.tokopedia.media.editor.ui.component.AddLogoToolUiComponent
import com.tokopedia.media.editor.ui.uimodel.EditorAddTextUiModel
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
        EditorToolType.ADD_TEXT -> R.string.editor_tool_add_text
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

fun addTextToText(textDetail: EditorAddTextUiModel?): String {
    return textDetail?.let { textValue ->
        var text = ""

        text += when (textValue.textTemplate) {
            EditorAddTextUiModel.TEXT_TEMPLATE_FREE -> ADD_TEXT_BEBAS
            else -> ADD_TEXT_TEMPLATE
        }

        text += "_" + when (textValue.textColor) {
            EditorAddTextUiModel.TEXT_COLOR_BLACK -> ADD_TEXT_COLOR_BLACK
            else -> ADD_TEXT_COLOR_WHITE
        }

        textValue.getLatarTemplate()?.let {
            text += "_${it.latarModel}"
        }

        text += when (textValue.textPosition) {
            EditorAddTextUiModel.TEXT_POSITION_TOP -> ADD_TEXT_POSITION_TOP
            EditorAddTextUiModel.TEXT_POSITION_RIGHT -> ADD_TEXT_POSITION_RIGHT
            EditorAddTextUiModel.TEXT_POSITION_BOTTOM -> ADD_TEXT_POSITION_BOTTOM
            else -> ADD_TEXT_POSITION_LEFT
        }

        return text
    } ?: run{
        ""
    }
}
