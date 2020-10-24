package com.tokopedia.phoneverification.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.phoneverification.data.model.response.PhoneVerificationResponseData
import com.tokopedia.phoneverification.domain.usecase.VerifyPhoneUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class PhoneVerificationViewModel @Inject constructor(
        private val verifyPhoneUseCase: VerifyPhoneUseCase,
        dispatcher: CoroutineDispatcher
) : BaseViewModel(dispatcher) {

    private val _response = MutableLiveData<Result<PhoneVerificationResponseData>>()
    val responseLiveData: LiveData<Result<PhoneVerificationResponseData>>
        get() = _response

    fun verifyPhone(phone: String) {
        verifyPhoneUseCase.verifyPhone(phone, onSuccessVerifyPhone(), onErrorVerifyPhone())
    }

    private fun onSuccessVerifyPhone(): (PhoneVerificationResponseData) -> Unit {
        return { response ->
            _response.postValue(Success(response))
        }
    }

    private fun onErrorVerifyPhone(): (Throwable) -> Unit {
        return { error ->
            _response.postValue(Fail(error))
        }
    }
}