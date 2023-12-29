package com.tokopedia.media.editor.data.entity

enum class AddTextPosition(val value: Int) {
    LEFT(0), RIGHT(1), TOP(2), BOTTOM(3);

    companion object {
        fun getPositionByIndex(index: Int): AddTextPosition {
            return AddTextPosition.values().first { it.value == index }
        }
    }
}
