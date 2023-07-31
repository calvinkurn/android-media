package com.tokopedia.editor.ui.main

import com.tokopedia.editor.data.model.NavigationTool
import com.tokopedia.picker.common.UniversalEditorParam

data class MainEditorUiModel(
    val param: UniversalEditorParam = UniversalEditorParam(),
    val tools: List<NavigationTool> = emptyList()
)
