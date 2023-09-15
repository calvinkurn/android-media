package com.tokopedia.editor.ui.main

import com.tokopedia.editor.data.model.NavigationTool
import com.tokopedia.editor.ui.model.ImagePlacementModel
import com.tokopedia.picker.common.UniversalEditorParam

/**
 * State nor UiModel of MainEditor.
 */
data class MainEditorUiModel(
    val param: UniversalEditorParam = UniversalEditorParam(),
    val tools: List<NavigationTool> = emptyList(),
    val activeFilePath: String = "",
    val imagePlacementModel: ImagePlacementModel? = null,
    val hasTextAdded: Boolean = false,
    val isRemoveAudio: Boolean = false,
) {

    fun hasPlacementEdited(): Boolean {
        return imagePlacementModel?.path != null && imagePlacementModel.path.isNotEmpty()
    }
}
