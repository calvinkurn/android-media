package com.tokopedia.editor.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.editor.util.TIMBER_TAG
import com.tokopedia.picker.common.UniversalEditorParam
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class MainEditorViewModel @Inject constructor(
    private val paramFetcher: EditorParamFetcher
) : ViewModel() {

    private val _param = MutableStateFlow<UniversalEditorParam?>(null)
    val param = _param.asStateFlow()

    init {
        viewModelScope.launch {
            _param.value = paramFetcher().also {
                Timber.d("$TIMBER_TAG: $it")
            }
        }
    }

    fun setParam(param: UniversalEditorParam) {
        viewModelScope.launch {
            paramFetcher.set(param)
        }
    }
}
