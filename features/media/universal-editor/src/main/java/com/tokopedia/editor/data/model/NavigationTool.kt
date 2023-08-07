package com.tokopedia.editor.data.model

import com.tokopedia.picker.common.types.ToolType

data class NavigationTool(
    @ToolType val type: Int,
    val name: String,
    val iconId: Int,
    val isSelected: Boolean = false
)
