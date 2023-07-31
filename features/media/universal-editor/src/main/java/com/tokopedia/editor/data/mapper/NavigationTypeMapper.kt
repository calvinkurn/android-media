package com.tokopedia.editor.data.mapper

import com.tokopedia.editor.R
import com.tokopedia.editor.data.model.NavigationTool
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.picker.common.types.ToolType

object NavigationTypeMapper {

    operator fun invoke(@ToolType type: Int): Pair<Int, Int> {
        return when (type) {
            ToolType.TEXT -> Pair(
                R.string.universal_editor_nav_tool_text,
                IconUnify.TEXT
            )
            ToolType.PLACEMENT -> Pair(
                R.string.universal_editor_nav_tool_placement,
                IconUnify.CROP
            )
            ToolType.AUDIO_MUTE -> Pair(
                R.string.universal_editor_nav_tool_audio_mute,
                IconUnify.VOLUME_MUTE
            )
            ToolType.TRIM -> Pair(
                R.string.universal_editor_nav_tool_trim,
                IconUnify.TRIM
            )
            else -> Pair(0, 0)
        }
    }

    fun Pair<Int, Int>.to(invoke: (Int) -> String): NavigationTool {
        return NavigationTool(invoke(first), second)
    }
}
