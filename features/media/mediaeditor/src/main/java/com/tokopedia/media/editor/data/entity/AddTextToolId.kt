package com.tokopedia.media.editor.data.entity

enum class AddTextToolId(val value: Int) {
    CHANGE_POSITION_INDEX(0),
    SAVE_TEMPLATE_INDEX(1),
    DIVIDER(2),
    FREE_TEXT_INDEX(3),
    BACKGROUND_TEXT_INDEX(4);

    companion object {
        fun getToolIdByIndex(index: Int): AddTextToolId {
            return values().first { it.value == index }
        }
    }
}
