package com.tokopedia.managepassword.changepassword.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.managepassword.changepassword.domain.data.ChangePasswordRequestModel
import com.tokopedia.managepassword.changepassword.domain.data.ChangePasswordResponseModel
import com.tokopedia.managepassword.changepassword.domain.usecase.ChangePasswordUseCase
import com.tokopedia.managepassword.common.ManagePasswordConstant
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.sessioncommon.domain.usecase.GeneratePublicKeyUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.coroutines.Result
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class ChangePasswordViewModel @Inject constructor(
        private val usecase: ChangePasswordUseCase,
        private val generatePublicKeyUseCase: GeneratePublicKeyUseCase,
        dispatcher: CoroutineDispatcher
) : BaseViewModel(dispatcher) {

    private val _response = MutableLiveData<Result<ChangePasswordResponseModel>>()
    val response: LiveData<Result<ChangePasswordResponseModel>>
        get() = _response

    private val _validatePassword = MutableLiveData<LiveDataValidateResult>()
    val validatePassword: LiveData<LiveDataValidateResult>
        get() = _validatePassword

    fun validatePassword(old: String, new: String, confirmation: String) {
        when {
            old.isEmpty() && new.isEmpty() && confirmation.isEmpty() -> {
                _validatePassword.postValue(LiveDataValidateResult.EMPTY_PARAMS)
            }
            new == old -> {
                _validatePassword.postValue(LiveDataValidateResult.SAME_WITH_OLD)
            }
            new != confirmation -> {
                _validatePassword.postValue(LiveDataValidateResult.CONFIRMATION_INVALID)
            }
            else -> {
                _validatePassword.postValue(LiveDataValidateResult.VALID)
            }
        }
    }

    fun submitChangePassword(encode: String, new: String, confirmation: String, validationToken: String) {
        launchCatchError(coroutineContext, {
            val result = generatePublicKeyUseCase.executeOnBackground()
            if(result.keyData.hash.isNotEmpty()) {
                usecase.params = createRequestParams(ChangePasswordRequestModel(encode, new, confirmation, validationToken, hash = result.keyData.hash))
                val changePassResponse = usecase.executeOnBackground()
                _response.postValue(Success(changePassResponse))
            }else {
                _response.postValue(Fail(Throwable("")))
            }
        }, {
            _response.postValue(Fail(it))
        })
    }

    companion object {
        fun createRequestParams(requestModel: ChangePasswordRequestModel): Map<String, Any> {
            return mapOf(ManagePasswordConstant.PARAM_INPUT to requestModel)
        }
    }
}