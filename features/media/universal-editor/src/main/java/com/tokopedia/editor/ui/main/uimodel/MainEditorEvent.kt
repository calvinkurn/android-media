package com.tokopedia.editor.ui.main.uimodel

import com.tokopedia.editor.ui.model.EditorModel
import com.tokopedia.editor.ui.model.ImagePlacementModel
import com.tokopedia.editor.ui.model.InputTextModel
import com.tokopedia.picker.common.UniversalEditorParam

/**
 * Event comes from user interaction. State transition are triggered by Events.
 */
sealed class MainEditorEvent {
    data class SetupView(val param: UniversalEditorParam) : MainEditorEvent()

    object AddInputTextPage : MainEditorEvent()

    data class EditInputTextPage(
        val typographyId: Int,
        val model: InputTextModel
    ) : MainEditorEvent()

    data class InputTextResult(val model: InputTextModel) : MainEditorEvent()

    object ResetActiveInputText : MainEditorEvent()

    object PlacementImagePage : MainEditorEvent()

    data class PlacementImageResult(val model: ImagePlacementModel?) : MainEditorEvent()
}
