package com.tokopedia.tokofood.example

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    val outputFlow = inputFlow
        .debounce(300)
        .distinctUntilChanged()
        .flatMapLatest {
            flow {
                emit(Result.Loading())
                val result = DummyRepository.processText(it)
                emit(Result.Success(result))
            }.catch {
                emit(Result.Failure(it))
            }
        }
        .shareIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 1)

    fun setInput(input: String) {
        inputFlow.tryEmit(input)
    }

    fun refresh() {
        inputFlow.tryEmit(getLatestInput())
    }

    fun getLatestInput(): String {
        return inputFlow.replayCache.firstOrNull() ?: ""
    }

    fun onSavedInstanceState(){
        savedStateHandle[INPUT_KEY] = inputFlow.replayCache.firstOrNull()
    }

    fun onRestoreSavednstanceState(){
        inputFlow.tryEmit(savedStateHandle[INPUT_KEY]?: "")
    }
}
