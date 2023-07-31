package com.tokopedia.editor.ui.main

import com.tokopedia.picker.common.UniversalEditorParam

sealed class MainEditorEvent {
    data class SetParam(val param: UniversalEditorParam): MainEditorEvent()
    object GetNavigationTool: MainEditorEvent()
}
