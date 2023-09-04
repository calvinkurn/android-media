package com.tokopedia.editor.ui.main.uimodel

import com.tokopedia.editor.ui.model.InputTextModel

/**
 * Effects are impure, Effects uses for user intent nor external events such as
 * Global State Changes, Navigate with Intent, Network Changes, etc.
 */
sealed class MainEditorEffect {
    data class OpenInputText(val model: InputTextModel) : MainEditorEffect()

    data class ParentToolbarVisibility(val visible: Boolean) : MainEditorEffect()
}
