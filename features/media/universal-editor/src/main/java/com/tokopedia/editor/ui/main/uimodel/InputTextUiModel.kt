package com.tokopedia.editor.ui.main.uimodel

import com.tokopedia.editor.ui.model.InputTextModel

data class InputTextUiModel(
    val isEdited: Boolean = false,
    val previousString: String = "",
    val model: InputTextModel? = null
) {

    companion object {
        fun reset() = InputTextUiModel()
    }
}
