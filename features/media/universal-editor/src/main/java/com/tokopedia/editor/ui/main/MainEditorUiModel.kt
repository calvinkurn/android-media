package com.tokopedia.editor.ui.main

import com.tokopedia.editor.data.model.NavigationTool
import com.tokopedia.editor.ui.model.EditorModel
import com.tokopedia.editor.ui.model.ImageModel
import com.tokopedia.editor.ui.model.VideoModel
import com.tokopedia.picker.common.UniversalEditorParam

/**
 * State nor UiModel of MainEditor.
 */
data class MainEditorUiModel(
    val param: UniversalEditorParam = UniversalEditorParam(),
    val tools: List<NavigationTool> = emptyList(),
    val imageModel: ImageModel? = null,
    val videoModel: VideoModel? = null
)
