package com.tokopedia.loginfingerprint.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.loginfingerprint.data.model.ValidateFingerprintResult
import com.tokopedia.loginfingerprint.data.preference.FingerprintPreferenceHelper
import com.tokopedia.loginfingerprint.domain.usecase.ValidateFingerprintUseCase
import com.tokopedia.loginfingerprint.utils.CryptographyUtils
import com.tokopedia.loginfingerprint.utils.DispatcherProvider
import com.tokopedia.sessioncommon.ErrorHandlerSession
import com.tokopedia.sessioncommon.data.LoginTokenPojo
import com.tokopedia.sessioncommon.domain.subscriber.LoginTokenSubscriber
import com.tokopedia.sessioncommon.domain.usecase.LoginTokenUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created by Yoris Prayogo on 2020-01-23.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

class ScanFingerprintViewModel @Inject constructor(dispatcher: DispatcherProvider,
                                                   private val userSession: UserSessionInterface,
                                                   private val cryptographyUtils: CryptographyUtils,
                                                   private val fingerprintPreferenceHelper: FingerprintPreferenceHelper,
                                                   private val loginTokenUseCase: LoginTokenUseCase,
                                                   private val validateFingerprintUseCase: ValidateFingerprintUseCase)
    : BaseViewModel(dispatcher.io()) {

    private val mutableLoginFingerprintResult = MutableLiveData<com.tokopedia.usecase.coroutines.Result<LoginTokenPojo>>()
    val loginFingerprintResult: LiveData<com.tokopedia.usecase.coroutines.Result<LoginTokenPojo>>
        get() = mutableLoginFingerprintResult

    val loginSubscriber =  LoginTokenSubscriber(userSession, onSuccessLoginToken(),
            onErrorValidateFP(), {}, {})

    fun validateFingerprint() {
        val signature = cryptographyUtils.generateFingerprintSignature(fingerprintPreferenceHelper.getFingerprintUserId(), userSession.deviceId)
        val param = validateFingerprintUseCase.createRequestParams(fingerprintPreferenceHelper.getFingerprintUserId(), signature)
        validateFingerprintUseCase.executeUseCase(param, onSuccessValidateFP(), onErrorValidateFP())
    }

    fun loginToken(validateToken: String){
        val param = LoginTokenUseCase.generateParamForFingerprint(validateToken, fingerprintPreferenceHelper.getFingerprintUserId())
        loginTokenUseCase.executeLoginFingerprint(param, loginSubscriber)
    }

    private fun onSuccessLoginToken(): (LoginTokenPojo) -> Unit {
        return { mutableLoginFingerprintResult.value = Success(it) }
    }

    private fun onSuccessValidateFP(): (ValidateFingerprintResult) -> Unit {
        return {
            val errorMessage = it.errorMessage
            val isSuccess = it.success
            if (errorMessage.isBlank() && isSuccess) {
                loginToken(it.validateToken)
            } else if (!errorMessage.isBlank()) {
                mutableLoginFingerprintResult.value = Fail(MessageErrorException(errorMessage,
                        ErrorHandlerSession.ErrorCode.WS_ERROR.toString()))
            } else {
                mutableLoginFingerprintResult.value = Fail(RuntimeException())
            }
        }
    }

    private fun onErrorValidateFP(): (Throwable) -> Unit {
        return {
            it.printStackTrace()
            mutableLoginFingerprintResult.postValue(Fail(it))
        }
    }
}