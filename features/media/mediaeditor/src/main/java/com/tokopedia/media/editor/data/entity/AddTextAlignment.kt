package com.tokopedia.media.editor.data.entity

enum class AddTextAlignment(val value: Int) {
    CENTER(0), RIGHT(1), LEFT(2);

    companion object {
        private val data = values().associateBy(AddTextAlignment::value)
        private fun getToolIdByIndex(index: Int) = data[index]

        fun AddTextAlignment.increaseIndex(): AddTextAlignment {
            var newIndex = value + 1
            if (newIndex > AddTextAlignment.data.size) {
                newIndex = CENTER.value
            }

            return getToolIdByIndex(newIndex) ?: CENTER
        }
    }
}
