package com.tokopedia.editor.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.editor.data.repository.NavigationToolRepository
import com.tokopedia.editor.ui.model.EditorModel
import com.tokopedia.editor.ui.model.ImageModel
import com.tokopedia.editor.ui.model.ImagePlacementModel
import com.tokopedia.editor.ui.model.InputTextModel
import com.tokopedia.editor.ui.model.VideoModel
import com.tokopedia.picker.common.UniversalEditorParam
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainEditorViewModel @Inject constructor(
    private val navigationToolRepository: NavigationToolRepository,
    private val paramFetcher: EditorParamFetcher
) : ViewModel() {

    private var _event = MutableSharedFlow<MainEditorEvent>(replay = 50)

    private var _state = MutableStateFlow(MainEditorUiModel())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            _event.collect { event ->
                when (event) {
                    is MainEditorEvent.SetupView -> {
                        setParam(event.param)
                        initEditorModel(event.param)
                        setAction(MainEditorEvent.GetNavigationTool)
                    }
                    is MainEditorEvent.GetNavigationTool -> {
                        getNavigationTool()
                    }
                }
            }
        }
    }

    fun setAction(action: MainEditorEvent) {
        _event.tryEmit(action)
    }

    fun updatePlacement(placementModel: ImagePlacementModel) {
        _state.update {
            val model = it.model?.clone()
            model?.image?.placement = placementModel

            it.copy(model = model)
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

    private fun initEditorModel(param: UniversalEditorParam) {
        val file = param.firstFile

        val model = if (file.isImage()) {
            EditorModel(image = ImageModel())
        } else {
            EditorModel(video = VideoModel())
        }

        _state.update {
            it.copy(model = model)
        }
    }

    private fun getNavigationTool() {
        _state.update {
            it.copy(tools = navigationToolRepository.tools())
        }
    }

    private fun setParam(param: UniversalEditorParam) {
        paramFetcher.set(param)

        _state.update {
            it.copy(param = param)
        }
    }
}
