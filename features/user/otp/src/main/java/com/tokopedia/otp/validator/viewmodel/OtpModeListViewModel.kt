package com.tokopedia.otp.validator.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.otp.validator.data.ModeListData
import com.tokopedia.otp.validator.data.OtpParams
import com.tokopedia.otp.validator.domain.usecase.OtpModeListUseCase
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

/**
 * @author rival
 * @created on 9/12/2019
 */

class OtpModeListViewModel @Inject constructor(
        private val otpModeListUseCase: OtpModeListUseCase,
        dispatcher: CoroutineDispatcher
) : BaseViewModel(dispatcher) {

    private val _modeListResponse = MutableLiveData<MutableList<ModeListData>>()
    val modeListResponse: LiveData<MutableList<ModeListData>>
        get() = _modeListResponse

    private val _error = MutableLiveData<Throwable>()
    val error: LiveData<Throwable>
        get() = _error

    fun getMethodList(otpParams: OtpParams) {
        launchCatchError(block = {
            otpModeListUseCase.createParams(OtpModeListUseCase.createRequestParams(otpParams))
            val result = otpModeListUseCase.executeOnBackground()
            _modeListResponse.postValue(result.data.modeList)
        }, onError = {
            _error.postValue(it)
        })
    }

    override fun onCleared() {
        super.onCleared()
        otpModeListUseCase.cancelJobs()
    }
}