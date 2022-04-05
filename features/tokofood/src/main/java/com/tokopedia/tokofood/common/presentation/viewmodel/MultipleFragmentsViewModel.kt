package com.tokopedia.tokofood.common.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.tokofood.common.util.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*

@FlowPreview
@ExperimentalCoroutinesApi
class MultipleFragmentsViewModel(val savedStateHandle: SavedStateHandle) : ViewModel() {
    val inputFlow = MutableSharedFlow<String>(1)

    companion object {
        const val INPUT_KEY = "string_input"
    }

    fun onSavedInstanceState() {
        savedStateHandle[INPUT_KEY] = inputFlow.replayCache.firstOrNull()
    }

    fun onRestoreSavednstanceState() {
        inputFlow.tryEmit(savedStateHandle[INPUT_KEY] ?: "")
    }
}
