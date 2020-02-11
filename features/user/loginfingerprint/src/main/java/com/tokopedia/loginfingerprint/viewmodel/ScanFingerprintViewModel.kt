package com.tokopedia.loginfingerprint.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.loginfingerprint.data.model.ValidateFingerprintPojo
import com.tokopedia.loginfingerprint.data.model.ValidateFingerprintResult
import com.tokopedia.loginfingerprint.di.LoginFingerprintQueryConstant
import com.tokopedia.loginfingerprint.domain.usecase.ValidateFingerprintUseCase
import com.tokopedia.loginfingerprint.utils.CryptographyUtils
import com.tokopedia.sessioncommon.ErrorHandlerSession
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

/**
 * Created by Yoris Prayogo on 2020-01-23.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

class ScanFingerprintViewModel @Inject constructor(dispatcher: CoroutineDispatcher,
                                                   private val userSession: UserSessionInterface,
                                                   val cryptographyUtils: CryptographyUtils,
                                                   private val validateFingerprintUseCase: ValidateFingerprintUseCase)
    : BaseViewModel(dispatcher) {

    private val mutableValidateFingerprintResult = MutableLiveData<com.tokopedia.usecase.coroutines.Result<ValidateFingerprintResult>>()
    val verifyValidateFingerprintResult: LiveData<com.tokopedia.usecase.coroutines.Result<ValidateFingerprintResult>>
        get() = mutableValidateFingerprintResult

    fun validateFingerprint() {
        val signature = cryptographyUtils.generateFingerprintSignature(userSession.userId, userSession.deviceId)
        val param = mapOf(
                LoginFingerprintQueryConstant.PARAM_OTP_TYPE to "145",
                LoginFingerprintQueryConstant.PARAM_SIGNATURE to signature.signature,
                LoginFingerprintQueryConstant.PARAM_TIME_UNIX to signature.datetime
        )
        validateFingerprintUseCase.setRequestParams(param)
        validateFingerprintUseCase.executeUseCase(onSuccessValidateFP(), onErrorValidateFP())
    }

    private fun onSuccessValidateFP(): (ValidateFingerprintResult) -> Unit {
        return {
            val errorMessage = it.errorMessage
            val isSuccess = it.success

            if (errorMessage.isBlank() && isSuccess) {
                mutableValidateFingerprintResult.value = Success(it)
            } else if (!errorMessage.isBlank()) {
                mutableValidateFingerprintResult.value = Fail(MessageErrorException(errorMessage,
                        ErrorHandlerSession.ErrorCode.WS_ERROR.toString()))
            } else {
                mutableValidateFingerprintResult.value = Fail(RuntimeException())
            }
        }
    }

    private fun onErrorValidateFP(): (Throwable) -> Unit {
        return {
            it.printStackTrace()
            mutableValidateFingerprintResult.postValue(Fail(it))
        }
    }

}