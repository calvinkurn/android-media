package com.tokopedia.chooseaccount.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.sessioncommon.data.PopupError
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success

open class BaseChooseAccountViewModel(
        dispatcher: CoroutineDispatchers
): BaseViewModel(dispatcher.main) {

    private val mutablePopupError = MutableLiveData<Result<PopupError>>()
    val popupError: LiveData<Result<PopupError>>
        get() = mutablePopupError

    private val mutableActivationPage = MutableLiveData<Result<MessageErrorException>>()
    val activationPage: LiveData<Result<MessageErrorException>>
        get() = mutableActivationPage

    private val mutableSecurityQuestion = MutableLiveData<Result<String>>()
    val securityQuestion: LiveData<Result<String>>
        get() = mutableSecurityQuestion

    protected fun onHasPopupError(popupError: PopupError) {
        mutablePopupError.value = Success(popupError)
    }

    protected fun onNeedActivation(message: MessageErrorException) {
        mutableActivationPage.value = Success(message)
    }

    protected fun onSecurityCheck(phone: String) {
        mutableSecurityQuestion.value = Success(phone)
    }
}