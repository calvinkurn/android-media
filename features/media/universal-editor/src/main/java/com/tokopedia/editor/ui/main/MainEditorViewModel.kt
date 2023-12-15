package com.tokopedia.editor.ui.main

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.editor.data.repository.NavigationToolRepository
import com.tokopedia.editor.data.repository.VideoFlattenRepository
import com.tokopedia.editor.R
import com.tokopedia.editor.analytics.main.editor.MainEditorAnalytics
import com.tokopedia.editor.data.repository.ImageFlattenRepository
import com.tokopedia.editor.data.repository.FlattenParam
import com.tokopedia.editor.ui.model.ImagePlacementModel
import com.tokopedia.editor.ui.main.uimodel.InputTextParam
import com.tokopedia.editor.ui.main.uimodel.MainEditorEffect
import com.tokopedia.editor.ui.main.uimodel.MainEditorEvent
import com.tokopedia.editor.ui.model.InputTextModel
import com.tokopedia.editor.util.provider.ResourceProvider
import com.tokopedia.editor.util.setValue
import com.tokopedia.picker.common.UniversalEditorParam
import com.tokopedia.picker.common.utils.MediaFile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainEditorViewModel @Inject constructor(
    private val navigationToolRepository: NavigationToolRepository,
    private val videoFlattenRepository: VideoFlattenRepository,
    private val imageFlattenRepository: ImageFlattenRepository,
    private val resourceProvider: ResourceProvider,
    private val dispatchers: CoroutineDispatchers,
    private val paramFetcher: EditorParamFetcher,
    private val analytics: MainEditorAnalytics
) : ViewModel() {

    private var _uiEffect = MutableSharedFlow<MainEditorEffect>(replay = 50)
    private var _mainEditorState = MutableStateFlow(MainEditorUiModel())
    private var _inputTextState = MutableStateFlow(InputTextParam())

    /**
     * A path is active file path.
     *
     * Whether comes from image placement, video source file, etc.
     * we need to set this [filePath] immediately as of current file.
     *
     * every single editing tool needs to update this filePath through
     * [setActiveEditableFilePath].
     *
     * This variable will be deleted if we want to support multi-file with drawer.
     */
    val filePath: String
        get() = _mainEditorState.value.activeFilePath

    val mainEditorState = _mainEditorState.asStateFlow()
    val inputTextState = _inputTextState.asStateFlow()

    val uiEffect: Flow<MainEditorEffect>
        get() = _uiEffect

    fun onEvent(event: MainEditorEvent) {
        when (event) {
            is MainEditorEvent.SetupView -> {
                prepareSetupView(event.param)
                renderNavigationTools()
            }
            is MainEditorEvent.HasTextAdded -> {
                updateTextAddedState(event.isAdded)
            }
            is MainEditorEvent.AddInputTextPage -> {
                analytics.toolTextClick()

                setAction(MainEditorEffect.OpenInputText(InputTextModel.default()))
            }
            is MainEditorEvent.EditInputTextPage -> {
                setAction(MainEditorEffect.OpenInputText(event.model, event.viewId))
                updateViewIdOnUiParam(event.viewId)
            }
            is MainEditorEvent.ExportMedia -> {
                mainEditorState.value.let {
                    analytics.finishPageClick(
                        hasText = it.hasTextAdded,
                        isMute = it.isRemoveAudio,
                        isCropped = it.hasPlacementEdited()
                    )
                }

                exportFinalMedia(filePath, event.canvasTextBitmap, event.imageBitmap)
            }
            is MainEditorEvent.PlacementImagePage -> {
                analytics.toolAdjustCropClick()
                navigateToPlacementPage()
            }
            is MainEditorEvent.PlacementImageResult -> {
                val model = event.model ?: return
                getPlacementResult(model)
            }
            is MainEditorEvent.InputTextResult -> {
                getInputTextResult(event.model)
            }
            is MainEditorEvent.ManageVideoAudio -> {
                removeVideoAudio()
            }
            is MainEditorEvent.ResetActiveInputText -> {
                _inputTextState.value = InputTextParam.reset()
            }
            is MainEditorEvent.DisposeRemainingTasks -> {
                videoFlattenRepository.cancel()
            }
            is MainEditorEvent.ClickHeaderCloseButton -> {
                analytics.backPageClick()

                val isPlacementEdited = mainEditorState.value.hasPlacementEdited()
                val isTextAdded = mainEditorState.value.hasTextAdded

                val isFlattenOngoing = if (MediaFile(filePath).isVideo()) {
                    videoFlattenRepository.isFlattenOngoing()
                } else true

                if ((isPlacementEdited || isTextAdded || isFlattenOngoing) && !event.isSkipConfirmation) {
                    setAction(MainEditorEffect.ShowCloseDialogConfirmation)
                } else {
                    setAction(MainEditorEffect.CloseMainEditorPage)
                }
            }
        }
    }

    private fun exportFinalMedia(filePath: String, canvasTextBitmap: Bitmap, imageBitmap: Bitmap?) {
        val file = MediaFile(filePath)

        if (file.exist().not()) return
        val path = file.path ?: return

        setAction(MainEditorEffect.ShowLoading)

        if (file.isImage()) {
            flattenImageFileWithTextCanvas(imageBitmap, canvasTextBitmap)
        } else {
            flattenVideoFileWithTextCanvas(path, canvasTextBitmap)
        }
    }

    private fun flattenVideoFileWithTextCanvas(videoPath: String, canvasTextBitmap: Bitmap) {
        val isRemoveAudio = mainEditorState.value.isRemoveAudio

        val param = FlattenParam(
            videoPath = videoPath,
            canvasTextBitmap = canvasTextBitmap,
            isRemoveAudio = isRemoveAudio
        )

        viewModelScope.launch {
            videoFlattenRepository
                .flatten(param)
                .flowOn(dispatchers.computation)
                .collect {
                    setAction(MainEditorEffect.HideLoading)

                    if (it.isNotEmpty()) {
                        setAction(MainEditorEffect.FinishEditorPage(it))
                    } else {
                        val message = resourceProvider.getString(R.string.universal_editor_flatten_error)
                        setAction(MainEditorEffect.ShowToastErrorMessage(message))
                    }
                }
        }
    }

    private fun flattenImageFileWithTextCanvas(imageBitmap: Bitmap?, canvasText: Bitmap) {
        if (imageBitmap == null) return

        viewModelScope.launch {
            imageFlattenRepository.flattenImage(imageBitmap, canvasText)
                .flowOn(dispatchers.computation)
                .collect {
                    setAction(MainEditorEffect.HideLoading)

                    if (it.isNotEmpty()) {
                        setAction(MainEditorEffect.FinishEditorPage(it))
                    } else {
                        val message = resourceProvider.getString(R.string.universal_editor_flatten_error)
                        setAction(MainEditorEffect.ShowToastErrorMessage(message))
                    }
                }
        }
    }

    private fun navigateToPlacementPage() {
        val sourceFilePath = paramFetcher.get().firstFile.path ?: return
        val currentPlacementModel = mainEditorState.value.imagePlacementModel

        setAction(MainEditorEffect.OpenPlacementPage(sourceFilePath, currentPlacementModel))
    }

    private fun getPlacementResult(model: ImagePlacementModel) {
        updateCurrentPlacementModel(model)
        setActiveEditableFilePath(model.path)
        setAction(MainEditorEffect.UpdatePagerSourcePath(model.path))
    }

    private fun getInputTextResult(model: InputTextModel) {
        updateModelOnUiParam(model)
        setAction(MainEditorEffect.UpdateTextAddedState)
    }

    private fun updateCurrentPlacementModel(model: ImagePlacementModel?) {
        _mainEditorState.setValue {
            copy(imagePlacementModel = model)
        }
    }

    private fun updateTextAddedState(isAdded: Boolean) {
        _mainEditorState.setValue {
            copy(hasTextAdded = isAdded)
        }
    }

    private fun updateViewIdOnUiParam(id: Int) {
        _inputTextState.setValue {
            copy(viewId = id)
        }
    }

    private fun updateModelOnUiParam(model: InputTextModel) {
        if (model.text.isEmpty()) return

        _inputTextState.setValue {
            copy(model = model)
        }
    }

    private fun renderNavigationTools() {
        _mainEditorState.setValue {
            copy(tools = navigationToolRepository.tools())
        }
    }

    private fun prepareSetupView(param: UniversalEditorParam) {
        paramFetcher.set(param)

        _mainEditorState.setValue {
            copy(param = param)
        }

        // initial state of file
        param.firstFile.path?.let {
            setActiveEditableFilePath(it)
        }
    }

    private fun removeVideoAudio() {
        val isAudio = !mainEditorState.value.isRemoveAudio

        _mainEditorState.setValue { copy(isRemoveAudio = isAudio) }
        setAction(MainEditorEffect.RemoveAudioState(isAudio))
    }

    private fun setActiveEditableFilePath(path: String) {
        _mainEditorState.setValue {
            copy(activeFilePath = path)
        }
    }

    private fun setAction(effect: MainEditorEffect) {
        _uiEffect.tryEmit(effect)
    }
}
