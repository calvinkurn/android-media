package com.tokopedia.editor.ui.main.uimodel

import com.tokopedia.editor.ui.model.ImagePlacementModel
import com.tokopedia.editor.ui.model.InputTextModel

/**
 * Effects are impure, Effects uses for user intent nor external events such as
 * Global State Changes, Navigate with Intent, Network Changes, etc.
 */
sealed class MainEditorEffect {
    // Intent
    data class OpenInputText(val model: InputTextModel) : MainEditorEffect()
    data class FinishEditorPage(val filePath: String) : MainEditorEffect()
    data class OpenPlacementPage(val sourcePath: String, val model: ImagePlacementModel?) : MainEditorEffect()

    object ShowCloseDialogConfirmation : MainEditorEffect()

    object CloseMainEditorPage : MainEditorEffect()

    // Visibility handler
    data class ShowToastErrorMessage(val message: String) : MainEditorEffect()
    data class ParentToolbarVisibility(val visible: Boolean) : MainEditorEffect()

    // Global loader
    object ShowLoading : MainEditorEffect()
    object HideLoading : MainEditorEffect()

    // Common
    object UpdateTextAddedState : MainEditorEffect()
    data class UpdatePagerSourcePath(val newSourcePath: String) : MainEditorEffect()
}
