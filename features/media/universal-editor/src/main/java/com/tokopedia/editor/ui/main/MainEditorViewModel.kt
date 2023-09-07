package com.tokopedia.editor.ui.main

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.editor.data.repository.NavigationToolRepository
import com.tokopedia.editor.data.repository.VideoFlattenRepository
import com.tokopedia.editor.R
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
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainEditorViewModel @Inject constructor(
    private val navigationToolRepository: NavigationToolRepository,
    private val videoFlattenRepository: VideoFlattenRepository,
    private val resourceProvider: ResourceProvider,
    private val dispatchers: CoroutineDispatchers,
    private val paramFetcher: EditorParamFetcher
) : ViewModel() {

    private var _uiEffect = MutableSharedFlow<MainEditorEffect>(replay = 50)
    private var _mainEditorState = MutableStateFlow(MainEditorUiModel())
    private var _inputTextState = MutableStateFlow(InputTextParam())

    /**
     * A path is active file path.
     *
     * Whether comes from image placement, video source file, etc.
     * we need to set this [activeFilePath] immediately as of current file.
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
                setAction(MainEditorEffect.OpenInputText(InputTextModel.default()))
                setAction(MainEditorEffect.ParentToolbarVisibility(false))
            }
            is MainEditorEvent.EditInputTextPage -> {
                setAction(MainEditorEffect.OpenInputText(event.model))
                setAction(MainEditorEffect.ParentToolbarVisibility(false))
                updateViewIdOnUiParam(event.viewId)
            }
            is MainEditorEvent.ExportMedia -> {
                setAction(MainEditorEffect.ShowLoading)
                exportFinalMedia(filePath, event.canvasTextBitmap)
            }
            is MainEditorEvent.PlacementImagePage -> {
                navigateToPlacementPage()
            }
            is MainEditorEvent.PlacementImageResult -> {
                val model = event.model ?: return
                getPlacementResult(model)
            }
            is MainEditorEvent.InputTextResult -> {
                getInputTextResult(event.model)
            }
            is MainEditorEvent.ResetActiveInputText -> {
                _inputTextState.value = InputTextParam.reset()
            }
            is MainEditorEvent.ClickHeaderClose -> {
                var isPlacementEdited: Boolean
                val isTextAdded = event.textNumber >= 1

                _mainEditorState.value.let {
                    isPlacementEdited = it.imagePlacementModel?.path != null
                }

                if ((isPlacementEdited || isTextAdded) && !event.isFinish) {
                    setAction(MainEditorEffect.ShowCloseDialogConfirmation)
                } else {
                    setAction(MainEditorEffect.CloseMainEditorPage)
                }
            }
        }
    }

    private fun exportFinalMedia(filePath: String, canvasText: Bitmap) {
        val file = MediaFile(filePath)

        if (file.exist().not()) return
        val path = file.path ?: return

        if (file.isImage()) {
            flattenImageFileWithTextCanvas(path, canvasText)
        } else {
            flattenVideoFileWithTextCanvas(path, canvasText)
        }
    }

    private fun flattenVideoFileWithTextCanvas(videoPath: String, canvasText: Bitmap) {
        viewModelScope.launch {
            videoFlattenRepository
                .flatten(videoPath, canvasText)
                .flowOn(dispatchers.computation)
                .onCompletion { setAction(MainEditorEffect.HideLoading) }
                .onEach {
                    if (it.isNotEmpty()) {
                        setAction(MainEditorEffect.FinishEditorPage(it))
                    } else {
                        val message = resourceProvider.getString(R.string.universal_editor_flatten_error)
                        setAction(MainEditorEffect.ShowToastErrorMessage(message))
                    }
                }
                .collect()
        }
    }

    private fun flattenImageFileWithTextCanvas(imagePath: String, canvasText: Bitmap) {
        // TODO: Will be handled by @calvin.
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
        setAction(MainEditorEffect.ParentToolbarVisibility(true))
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

    private fun setActiveEditableFilePath(path: String) {
        _mainEditorState.setValue {
            copy(activeFilePath = path)
        }
    }

    private fun setAction(effect: MainEditorEffect) {
        _uiEffect.tryEmit(effect)
    }
}
