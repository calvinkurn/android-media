package com.tokopedia.targetedticker.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.targetedticker.domain.GetTargetedTickerUseCase
import com.tokopedia.targetedticker.domain.TargetedTickerMapper
import com.tokopedia.targetedticker.domain.TargetedTickerParamModel
import com.tokopedia.targetedticker.domain.TickerModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import javax.inject.Inject

class TargetedTickerViewModel @Inject constructor(
    private val getTargetedTickerUseCase: GetTargetedTickerUseCase,
    dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    private val _tickerState = MutableLiveData<Result<TickerModel>>()
    val tickerState: LiveData<Result<TickerModel>>
        get() = _tickerState

    fun getTargetedTicker(targetedTickerParam: TargetedTickerParamModel = TargetedTickerParamModel()) {
        viewModelScope.launchCatchError(
            block = {
                val response = getTargetedTickerUseCase(targetedTickerParam)
                _tickerState.value = Success(
                    TargetedTickerMapper.convertTargetedTickerToUiModel(
                        targetedTickerData = response.getTargetedTickerData
                    )
                )
            },
            onError = {
                _tickerState.value = Fail(it)
            }
        )
    }
}
