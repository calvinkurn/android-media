package com.tokopedia.media.editor.data.entity

enum class AddTextBackgroundTemplate(val value: Int) {
    FULL(0), SIDE_CUT(1), FLOATING(2);

    companion object {
        fun getBackgroundModelByIndex(index: Int): AddTextBackgroundTemplate {
            return AddTextBackgroundTemplate.values().first { it.value ==  index}
        }
    }
}
