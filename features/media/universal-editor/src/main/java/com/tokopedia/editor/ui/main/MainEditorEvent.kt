package com.tokopedia.editor.ui.main

import com.tokopedia.editor.ui.model.InputTextModel
import com.tokopedia.picker.common.UniversalEditorParam

sealed class MainEditorEvent {
    data class SetupView(val param: UniversalEditorParam): MainEditorEvent()
    object GetNavigationTool: MainEditorEvent()

    data class InputText(val model: InputTextModel): MainEditorEvent()
}
