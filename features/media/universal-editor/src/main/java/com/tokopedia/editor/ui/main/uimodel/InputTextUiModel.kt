package com.tokopedia.editor.ui.main.uimodel

import com.tokopedia.editor.ui.model.InputTextModel

data class InputTextUiModel(
    val typographyId: Int = -1,
    val model: InputTextModel? = null
) {

    companion object {
        fun reset() = InputTextUiModel()
    }
}
