package com.tokopedia.editor.ui.main.uimodel

import com.tokopedia.editor.ui.model.InputTextModel
import com.tokopedia.picker.common.UniversalEditorParam

/**
 * Event comes from user interaction. State transition are triggered by Events.
 */
sealed class MainEditorEvent {
    data class SetupView(val param: UniversalEditorParam) : MainEditorEvent()

    data class ClickInputTextTool(
        val model: InputTextModel,
        val isEdited: IsEdited = IsEdited()
    ) : MainEditorEvent()

    data class InputTextResult(val model: InputTextModel) : MainEditorEvent()
}

@JvmInline
value class IsEdited(val value: Boolean = false)
