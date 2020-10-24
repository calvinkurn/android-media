package com.tokopedia.phoneverification.domain.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.phoneverification.data.VerifyPhoneNumberDomain
import com.tokopedia.usecase.coroutines.Result
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class VerifyPhoneViewModel @Inject constructor(
        dispatcher: CoroutineDispatcher
): BaseViewModel(dispatcher){

    private val _verifyPhoneResponse = MutableLiveData<Result<VerifyPhoneNumberDomain>>()
    val verifyPhoneResponse: LiveData<Result<VerifyPhoneNumberDomain>>
        get() = _verifyPhoneResponse


    fun verifyPhone(phoneNumber: String) {

    }
}