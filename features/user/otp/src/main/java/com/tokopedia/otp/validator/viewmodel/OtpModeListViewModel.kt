package com.tokopedia.otp.validator.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.otp.validator.data.ModeListData
import com.tokopedia.otp.validator.data.OtpParams
import com.tokopedia.otp.validator.domain.usecase.OtpModeListUseCase
import com.tokopedia.usecase.coroutines.Fail
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

    private val mutableOtpMethodListResponse = MutableLiveData<Result<MutableList<ModeListData>>>()
    val otpMethodListResponse: LiveData<Result<MutableList<ModeListData>>>
        get() = mutableOtpMethodListResponse

    fun getMethodList(otpParams: OtpParams) {
        launchCatchError(block = {
            otpModeListUseCase.createParams(OtpModeListUseCase.createRequestParams(otpParams))
            val result = otpModeListUseCase.executeOnBackground()
            mutableOtpMethodListResponse.postValue(Success(result.data.modeList))
        }, onError = {
            mutableOtpMethodListResponse.postValue(Fail(it))
        })
    }

    override fun onCleared() {
        super.onCleared()
        otpModeListUseCase.cancelJobs()
    }
}