package com.tokopedia.analyticsdebugger.sse.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.analyticsdebugger.sse.domain.usecase.GetSSELogUseCase
import com.tokopedia.analyticsdebugger.sse.ui.uimodel.SSELogUiModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on November 10, 2021
 */
class SSELoggingViewModel @Inject constructor(
    private val getSSELogUseCase: GetSSELogUseCase
): ViewModel() {

    private val _observableSSELog = MutableLiveData<List<SSELogUiModel>>()
    val observableSSELog: LiveData<List<SSELogUiModel>>
        get() = _observableSSELog

    fun getLog(query: String) {
        viewModelScope.launch {
            getSSELogUseCase.setParam(query)
            _observableSSELog.value = getSSELogUseCase.executeOnBackground()
        }
    }
}