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
        fun create(): List<ToolUiModel> {
            return listOf(
                ToolUiModel(EditorToolType.BRIGHTNESS, R.string.editor_tool_brightness, IconUnify.BRIGHTNESS),
                ToolUiModel(EditorToolType.CONTRAST, R.string.editor_tool_contrast, IconUnify.CONTRAST),
                ToolUiModel(EditorToolType.CROP, R.string.editor_tool_crop, IconUnify.CROP),
                ToolUiModel(EditorToolType.ROTATE, R.string.editor_tool_rotate, IconUnify.ROTATION),
            )
        }
    }

}