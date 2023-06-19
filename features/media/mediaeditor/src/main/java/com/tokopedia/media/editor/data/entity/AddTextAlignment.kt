package com.tokopedia.media.editor.data.entity

enum class AddTextAlignment(val value: Int) {
    CENTER(0), RIGHT(1), LEFT(2);

    companion object {
        private fun getToolIdByIndex(index: Int): AddTextAlignment {
            return AddTextAlignment.values().first { it.value == index }
        }

        fun AddTextAlignment.increaseIndex(): AddTextAlignment {
            return getToolIdByIndex(value+1)
        }
    }
}
