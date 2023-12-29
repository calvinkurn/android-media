package com.tokopedia.media.editor.data.entity
enum class AddTextTemplateMode(val value: Int) {
    FREE(0), BACKGROUND(1);

    companion object {
        fun templateToToolId(templateMode: Int): AddTextToolId {
            val tempTemplateMode =
                AddTextTemplateMode.values().firstOrNull { it.value == templateMode }

            return if (tempTemplateMode == FREE) {
                AddTextToolId.FREE_TEXT_INDEX
            } else {
                AddTextToolId.BACKGROUND_TEXT_INDEX
            }
        }
    }
}
