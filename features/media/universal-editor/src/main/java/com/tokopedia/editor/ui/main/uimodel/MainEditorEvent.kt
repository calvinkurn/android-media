package com.tokopedia.editor.ui.main.uimodel

import android.graphics.Bitmap
import com.tokopedia.editor.ui.model.ImagePlacementModel
import com.tokopedia.editor.ui.model.InputTextModel
import com.tokopedia.picker.common.UniversalEditorParam

/**
 * Event comes from user interaction. State transition are triggered by Events.
 */
sealed class MainEditorEvent {
    // Init
    data class SetupView(val param: UniversalEditorParam) : MainEditorEvent()

    // Handling State Behavior
    object ResetActiveInputText : MainEditorEvent()
    data class HasTextAdded(val isAdded: Boolean) : MainEditorEvent()
    object ManageVideoAudio : MainEditorEvent()

    // User Action
    data class ClickHeaderCloseButton(val isSkipConfirmation: Boolean = false) : MainEditorEvent()

    // External Intent
    object PlacementImagePage : MainEditorEvent()
    object AddInputTextPage : MainEditorEvent()
    data class EditInputTextPage(
        val viewId: Int,
        val model: InputTextModel
    ) : MainEditorEvent()

    // Intent result
    data class ExportMedia(val canvasTextBitmap: Bitmap, val imageBitmap: Bitmap? = null) : MainEditorEvent()
    data class InputTextResult(val model: InputTextModel) : MainEditorEvent()
    data class PlacementImageResult(val model: ImagePlacementModel?) : MainEditorEvent()
}
