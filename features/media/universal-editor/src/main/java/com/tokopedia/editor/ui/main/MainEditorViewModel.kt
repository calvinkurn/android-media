package com.tokopedia.editor.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.editor.data.repository.NavigationToolRepository
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

    private val _event = MutableSharedFlow<MainEditorEvent>(replay = 50)

    private val _state = MutableStateFlow(MainEditorUiModel())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            _event.collect { event ->
                when (event) {
                    is MainEditorEvent.SetParam -> {
                        setParam(event.param)
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
