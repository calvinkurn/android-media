package com.tokopedia.editor.ui.main.uimodel

import com.tokopedia.editor.ui.model.InputTextModel

data class InputTextParam(
    val viewId: Int = -1,
    val model: InputTextModel? = null
) {

    companion object {
        fun reset() = InputTextParam()
    }
}
