package com.tokopedia.media.editor.ui.adapter.addtext

data class AddTextAction(
    var textRef: Int,
    val iconId: Int? = null,
    val isDivider: Boolean = false,
    val iconRef: Int = 0,
    val isIconFull: Boolean = false
)
