package com.tokopedia.managepassword.changepassword.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.managepassword.changepassword.domain.data.ChangePasswordData
import com.tokopedia.managepassword.changepassword.domain.data.ChangePasswordRequestModel
import com.tokopedia.managepassword.changepassword.domain.usecase.ChangePasswordUseCase
import com.tokopedia.managepassword.changepassword.domain.usecase.ChangePasswordV2UseCase
import com.tokopedia.managepassword.common.ManagePasswordConstant
import com.tokopedia.sessioncommon.domain.usecase.GeneratePublicKeyUseCase
import com.tokopedia.sessioncommon.extensions.decodeBase64
import com.tokopedia.sessioncommon.util.RSAUtils
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class ChangePasswordViewModel @Inject constructor(
        private val usecase: ChangePasswordUseCase,
        private val changePasswordV2UseCase: ChangePasswordV2UseCase,
        private val generatePublicKeyUseCase: GeneratePublicKeyUseCase,
        dispatcher: CoroutineDispatcher
) : BaseViewModel(dispatcher) {

    private val _response = MutableLiveData<Result<ChangePasswordData>>()
    val response: LiveData<Result<ChangePasswordData>>
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

    fun submitChangePasswordV2(encode: String, new: String, confirmation: String, validationToken: String){
        launchCatchError(coroutineContext, {
            val result = generatePublicKeyUseCase.executeOnBackground()
            if(result.keyData.hash.isNotEmpty()) {
                val rsaUtils = RSAUtils()
                val encryptedPassword = rsaUtils.encrypt(new, result.keyData.key.decodeBase64(), true)
                val encryptedConfirmationPass = rsaUtils.encrypt(confirmation, result.keyData.key.decodeBase64(), true)
                changePasswordV2UseCase.setParams(encode, new = encryptedPassword, confirmation = encryptedConfirmationPass, validationToken, hash = result.keyData.hash)
                val changePassResponse = changePasswordV2UseCase.executeOnBackground()
                _response.postValue(Success(changePassResponse.changePassword))
            }else {
                _response.postValue(Fail(Throwable("")))
            }
        }, {
            _response.postValue(Fail(it))
        })
    }

    fun submitChangePassword(encode: String, new: String, confirmation: String, validationToken: String) {
        usecase.params = createRequestParams(ChangePasswordRequestModel(encode, new, confirmation, validationToken))
        usecase.submit(onSuccess = {
            _response.postValue(Success(it.changePassword))
        }, onError = {
            _response.postValue(Fail(it))
        })
    }

    companion object {
        fun createRequestParams(requestModel: ChangePasswordRequestModel): Map<String, Any> {
            return mapOf(ManagePasswordConstant.PARAM_INPUT to requestModel)
        }
    }
}