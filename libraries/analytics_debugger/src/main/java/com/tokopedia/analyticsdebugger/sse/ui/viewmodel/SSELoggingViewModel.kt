package com.tokopedia.analyticsdebugger.sse.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.analyticsdebugger.sse.domain.usecase.DeleteAllSSELogUseCase
import com.tokopedia.analyticsdebugger.sse.domain.usecase.GetSSELogUseCase
import com.tokopedia.analyticsdebugger.sse.ui.uimodel.SSELogUiModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on November 10, 2021
 */
class SSELoggingViewModel @Inject constructor(
    private val getSSELogUseCase: GetSSELogUseCase,
    private val deleteAllSSELogUseCase: DeleteAllSSELogUseCase,
): ViewModel() {

    private val _observableError = MutableLiveData<String>()
    val observableError: LiveData<String>
        get() = _observableError

    private val _observableSSELog = MutableLiveData<List<SSELogUiModel>>()
    val observableSSELog: LiveData<List<SSELogUiModel>>
        get() = _observableSSELog

    fun getLog(query: String) {
        viewModelScope.launchCatchError(block = {
            getSSELogUseCase.setParam(query)
            _observableSSELog.value = getSSELogUseCase.executeOnBackground()
        }) {
            _observableSSELog.value = emptyList()
            onError(it)
        }
    }

    fun deleteAllLog() {
        viewModelScope.launchCatchError(block = {
            deleteAllSSELogUseCase.executeOnBackground()
        }) {
            onError(it)
        }
    }

    private fun onError(throwable: Throwable) {
        _observableError.value = throwable.message ?: "Something went wrong"
    }
}