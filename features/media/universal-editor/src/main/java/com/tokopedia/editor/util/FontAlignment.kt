package com.tokopedia.editor.util

enum class FontAlignment(val value: Int) {
    CENTER(0), RIGHT(1), LEFT(2);

    companion object {
        private val data = values().associateBy(FontAlignment::value)

        private fun getToolIdByIndex(index: Int) = data[index]

        fun FontAlignment.next(): FontAlignment {
            var newIndex = value + 1
            if (newIndex > FontAlignment.data.size) {
                newIndex = CENTER.value
            }

            return getToolIdByIndex(newIndex) ?: CENTER
        }
    }
}
