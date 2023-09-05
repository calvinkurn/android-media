package com.tokopedia.editor.ui.main

import androidx.lifecycle.ViewModel
import com.tokopedia.editor.data.repository.NavigationToolRepository
import com.tokopedia.editor.ui.model.ImagePlacementModel
import com.tokopedia.editor.ui.main.uimodel.InputTextParam
import com.tokopedia.editor.ui.main.uimodel.MainEditorEffect
import com.tokopedia.editor.ui.main.uimodel.MainEditorEvent
import com.tokopedia.editor.ui.model.ImageModel
import com.tokopedia.editor.ui.model.InputTextModel
import com.tokopedia.editor.util.setValue
import com.tokopedia.picker.common.UniversalEditorParam
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class MainEditorViewModel @Inject constructor(
    private val navigationToolRepository: NavigationToolRepository,
    private val paramFetcher: EditorParamFetcher
) : ViewModel() {

    private var _uiEffect = MutableSharedFlow<MainEditorEffect>(replay = 50)
    private var _mainEditorState = MutableStateFlow(MainEditorUiModel())
    private var _inputTextState = MutableStateFlow(InputTextParam())

    val mainEditorState = _mainEditorState.asStateFlow()
    val inputTextState = _inputTextState.asStateFlow()

    val uiEffect: Flow<MainEditorEffect>
        get() = _uiEffect

    fun onEvent(event: MainEditorEvent) {
        when (event) {
            is MainEditorEvent.SetupView -> {
                setGlobalEditorParam(event.param)
                renderNavigationTools()
            }
            is MainEditorEvent.AddInputTextPage -> {
                setAction(MainEditorEffect.OpenInputText(InputTextModel.default()))
                setAction(MainEditorEffect.ParentToolbarVisibility(false))
            }
            is MainEditorEvent.EditInputTextPage -> {
                setAction(MainEditorEffect.OpenInputText(event.model))
                setAction(MainEditorEffect.ParentToolbarVisibility(false))
                updateViewIdOnUiParam(event.typographyId)
            }
            is MainEditorEvent.InputTextResult -> {
                updateModelOnUiParam(event.model)
                setAction(MainEditorEffect.ParentToolbarVisibility(true))
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

    private fun setGlobalEditorParam(param: UniversalEditorParam) {
        paramFetcher.set(param)

        _mainEditorState.setValue {
            copy(param = param)
        }
    }

    private fun setAction(effect: MainEditorEffect) {
        _uiEffect.tryEmit(effect)
    }
}
