package com.tokopedia.media.editor.utils

enum class AddTextToolId(val value: Int) {
    CHANGE_POSITION_INDEX(0),
    SAVE_TEMPLATE_INDEX(1),
    DIVIDER(2),
    FREE_TEXT_INDEX(3),
    BACKGROUND_TEXT_INDEX(4)
}

enum class AddTextPosition(val value: Int) {
    LEFT(0), RIGHT(1), TOP(2), BOTTOM(3)
}

enum class AddTextStyle(val value: Int) {
    REGULAR(0), BOLD(1), ITALIC(2)
}

enum class AddTextAlignment(val value: Int) {
    CENTER(0), RIGHT(1), LEFT(2)
}

enum class AddTextBackgroundTemplate(val value: Int) {
    FULL(0), SIDE_CUT(1), FLOATING(2)
}
