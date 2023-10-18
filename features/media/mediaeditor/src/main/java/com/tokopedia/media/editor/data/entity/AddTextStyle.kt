package com.tokopedia.media.editor.data.entity

enum class AddTextStyle(val value: Int) {
    REGULAR(0), BOLD(1), ITALIC(2);

    companion object {
        fun getStyleByIndex(index: Int): AddTextStyle {
            return AddTextStyle.values().first { it.value == index }
        }
    }
}
