package com.tokopedia.editor.ui.main

import androidx.lifecycle.ViewModel
import com.tokopedia.editor.data.repository.NavigationToolRepository
import com.tokopedia.editor.ui.main.uimodel.InputTextUiModel
import com.tokopedia.editor.ui.main.uimodel.MainEditorEffect
import com.tokopedia.editor.ui.main.uimodel.MainEditorEvent
import com.tokopedia.editor.ui.main.uimodel.MainEditorUiModel
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
    private var _inputTextState = MutableStateFlow(InputTextUiModel())

    val mainEditorState = _mainEditorState.asStateFlow()
    val inputTextState = _inputTextState.asStateFlow()

    val uiEffect: Flow<MainEditorEffect>
        get() = _uiEffect

    fun onEvent(event: MainEditorEvent) {
        when (event) {
            is MainEditorEvent.SetupView -> {
                setParam(event.param)
                fetchNavigationTool()
            }
            is MainEditorEvent.ClickInputTextTool -> {
                setAction(MainEditorEffect.OpenInputText(event.model))
                updateDataOnActiveText(event.model, event.isEdited)
            }
            is MainEditorEvent.InputTextResult -> {
                updateActiveInputTextData(event.model)
            }
            is MainEditorEvent.ResetActiveInputText -> {
                _inputTextState.value = InputTextUiModel.reset()
            }
        }
    }

    fun setTextState(textModel: InputTextModel) {
        val newState = _state.value.copy()

        // TODO: Need to adjust later to get editted text target and update the target instead of 1st item
        val textUpdateTarget = newState.model?.image?.texts?.toList()?.firstOrNull()?.first ?: textModel.text
        newState.model?.image?.texts?.set(textUpdateTarget, textModel)

        _state.update {
            newState
        }
    }

    private fun updateDataOnActiveText(model: InputTextModel, isEdited: Boolean) {
        _inputTextState.setValue {
            copy(
                isEdited = isEdited,
                previousString = model.text
            )
        }
    }

    private fun updateActiveInputTextData(model: InputTextModel) {
        _inputTextState.setValue {
            copy(model = model)
        }
    }

    private fun fetchNavigationTool() {
        _mainEditorState.setValue {
            copy(tools = navigationToolRepository.tools())
        }
    }

    private fun setParam(param: UniversalEditorParam) {
        paramFetcher.set(param)

        _mainEditorState.setValue {
            copy(param = param)
        }
    }

    private fun setAction(effect: MainEditorEffect) {
        _uiEffect.tryEmit(effect)
    }
}
