package com.tokopedia.editor.data.mapper

import com.tokopedia.editor.R
import com.tokopedia.editor.data.model.NavigationTool
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.picker.common.types.ToolType

object NavigationTypeMapper {

    operator fun invoke(@ToolType type: Int): Triple<Int, Int, Int> {
        return when (type) {
            ToolType.TEXT -> Triple(
                ToolType.TEXT,
                R.string.universal_editor_nav_tool_text,
                IconUnify.TEXT
            )
            ToolType.PLACEMENT -> Triple(
                ToolType.PLACEMENT,
                R.string.universal_editor_nav_tool_placement,
                IconUnify.CROP
            )
            ToolType.AUDIO_MUTE -> Triple(
                ToolType.AUDIO_MUTE,
                R.string.universal_editor_nav_tool_audio_mute,
                IconUnify.VOLUME_MUTE
            )
            ToolType.TRIM -> Triple(
                ToolType.TRIM,
                R.string.universal_editor_nav_tool_trim,
                IconUnify.TRIM
            )
            else -> Triple(0, 0, 0)
        }
    }

    fun Triple<Int, Int, Int>.to(invoke: (Int) -> String): NavigationTool {
        val (type, title, icon) = this
        return NavigationTool(type, invoke(title), icon)
    }
}
