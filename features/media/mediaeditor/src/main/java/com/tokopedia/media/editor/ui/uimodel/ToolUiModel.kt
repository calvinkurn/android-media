package com.tokopedia.media.editor.ui.uimodel

import com.tokopedia.iconunify.IconUnify
import com.tokopedia.media.editor.R
import com.tokopedia.picker.common.types.EditorToolType

data class ToolUiModel(
    @EditorToolType val id: Int,
    val name: Int,
    val icon: Int
) {

    companion object {
        private val labels = mapOf(
            EditorToolType.BRIGHTNESS to R.string.editor_tool_brightness,
            EditorToolType.CONTRAST to R.string.editor_tool_contrast,
            EditorToolType.CROP to R.string.editor_tool_crop,
            EditorToolType.ROTATE to R.string.editor_tool_rotate,
            EditorToolType.REMOVE_BACKGROUND to R.string.editor_tool_remove_background,
            EditorToolType.WATERMARK to R.string.editor_tool_watermark,
            EditorToolType.ADD_LOGO to R.string.editor_tool_add_logo,
            EditorToolType.ADD_TEXT to R.string.editor_tool_add_text
        )

        private val icons = mapOf(
            EditorToolType.BRIGHTNESS to IconUnify.BRIGHTNESS,
            EditorToolType.CONTRAST to IconUnify.CONTRAST,
            EditorToolType.CROP to IconUnify.CROP,
            EditorToolType.ROTATE to IconUnify.ROTATION,
            EditorToolType.REMOVE_BACKGROUND to IconUnify.BG_REMOVE,
            EditorToolType.WATERMARK to IconUnify.WATERMARK,
            EditorToolType.ADD_LOGO to IconUnify.STAMP,
            EditorToolType.ADD_TEXT to IconUnify.TEXT
        )

        fun List<Int>.create() = map {
            ToolUiModel(it, labels[it]!!, icons[it]!!)
        }
    }

}
