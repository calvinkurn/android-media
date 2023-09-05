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
import com.tokopedia.editor.ui.model.ImageModel
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
import kotlinx.coroutines.flow.onCompletion
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
    private var _errorMessageState = MutableStateFlow("")

    /**
     * A path is active file path.
     *
     * Whether comes from image placement, video source file, etc.
     * we need to set this [activeFilePath] immediately as of current file.
     */
    private val filePath
        get() = _mainEditorState.value.activeFilePath

    val mainEditorState = _mainEditorState.asStateFlow()
    val inputTextState = _inputTextState.asStateFlow()
    val errorMessageState = _errorMessageState.asStateFlow()

    val uiEffect: Flow<MainEditorEffect>
        get() = _uiEffect

    fun onEvent(event: MainEditorEvent) {
        when (event) {
            is MainEditorEvent.SetupView -> {
                prepareSetupView(event.param)
                renderNavigationTools()
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
            is MainEditorEvent.InputTextResult -> {
                updateModelOnUiParam(event.model)
                setAction(MainEditorEffect.ParentToolbarVisibility(true))
            }
            is MainEditorEvent.ExportMedia -> {
                setAction(MainEditorEffect.ShowLoading)
                exportFinalMedia(filePath, event.canvasTextBitmap)
            }
            is MainEditorEvent.ResetActiveInputText -> {
                _inputTextState.value = InputTextParam.reset()
            }
            is MainEditorEvent.PlacementImagePage -> {
                mainEditorState.value.let {
                    setAction(MainEditorEffect.OpenPlacementPage(
                        sourcePath = it.param.paths.first(),
                        model = it.imageModel?.placement
                    ))
                }
            }
            is MainEditorEvent.PlacementImageResult -> {
                event.model?.let {
                    _mainEditorState.setValue {
                        val newModel = this.imageModel?.apply { placement = it } ?: ImageModel(placement = it)
                        copy(imageModel = newModel)
                    }

                    // Tag will be replace with identifier for pager item
                    setAction(MainEditorEffect.UpdatePagerSourcePath(it.path, "Tag"))
                }
            }
        }
    }

    private fun exportFinalMedia(filePath: String, canvasText: Bitmap) {
        val file = MediaFile(filePath)
        if (file.exist().not()) return

        if (file.isImage()) {
            mergeImageAndTextAsFile(file.path, canvasText)
        } else {
            mergeVideoAndTextAsFile(file.path, canvasText)
        }
    }

    private fun mergeVideoAndTextAsFile(videoPath: String, canvasText: Bitmap) {
        viewModelScope.launch {
            videoFlattenRepository
                .flatten(videoPath, canvasText)
                .flowOn(dispatchers.computation)
                .onCompletion { setAction(MainEditorEffect.HideLoading)}
                .collect {
                    if (it.isNotEmpty()) {
                        setAction(MainEditorEffect.FinishEditorPage(it))
                    } else {
                        val message = resourceProvider.getString(R.string.universal_editor_flatten_error)
                        setErrorMessage(message)
                    }
                }
        }
    }

    private fun mergeImageAndTextAsFile(imagePath: String, canvasText: Bitmap) {
        // TODO
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
            copy(
                param = param,
                activeFilePath = param.firstFile.path
            )
        }
    }

    private fun setAction(effect: MainEditorEffect) {
        _uiEffect.tryEmit(effect)
    }

    private fun setErrorMessage(message: String) {
        _errorMessageState.value = message
    }
}
