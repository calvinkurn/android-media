package com.tokopedia.loginfingerprint.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.loginfingerprint.data.model.RegisterFingerprintPojo
import com.tokopedia.loginfingerprint.data.model.RegisterFingerprintResult
import com.tokopedia.loginfingerprint.data.preference.FingerprintPreferenceHelper
import com.tokopedia.loginfingerprint.domain.usecase.RegisterFingerprintUseCase
import com.tokopedia.loginfingerprint.utils.CryptographyUtils
import com.tokopedia.sessioncommon.ErrorHandlerSession
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

/**
 * Created by Yoris Prayogo on 2020-02-05.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

class RegisterOnboardingViewModel @Inject constructor(dispatcher: CoroutineDispatcher,
                                                      private val userSession: UserSessionInterface,
                                                      private val cryptographyUtils: CryptographyUtils,
                                                      private val fingerprintPreferenceHelper: FingerprintPreferenceHelper,
                                                      private val registerFingerprintUseCase: RegisterFingerprintUseCase)
    : BaseViewModel(dispatcher){

    private val mutableRegisterFingerprintResult = MutableLiveData<Result<RegisterFingerprintResult>>()
    val verifyRegisterFingerprintResult: LiveData<Result<RegisterFingerprintResult>>
        get() = mutableRegisterFingerprintResult

    fun registerFingerprint(){
        val signature = cryptographyUtils.generateFingerprintSignature(userSession.userId, userSession.deviceId)
        registerFingerprintUseCase.setRequestParams(registerFingerprintUseCase.createRequestParam(signature, cryptographyUtils.getPublicKey()))
        registerFingerprintUseCase.executeUseCase(onSuccessRegisterFP(), onErrorRegisterFP())
    }

    private fun onSuccessRegisterFP(): (RegisterFingerprintPojo) -> Unit {
        return {
            val errorMessage = it.data.errorMessage
            val isSuccess = it.data.success

            if (errorMessage.isBlank() && isSuccess) {
                mutableRegisterFingerprintResult.value = Success(it.data)
                fingerprintPreferenceHelper.registerFingerprint()
                fingerprintPreferenceHelper.saveUserId(userSession.userId)
            } else if (!errorMessage.isBlank()) {
                mutableRegisterFingerprintResult.value = Fail(MessageErrorException(errorMessage,
                        ErrorHandlerSession.ErrorCode.WS_ERROR.toString()))
            } else {
                mutableRegisterFingerprintResult.value = Fail(RuntimeException())
            }
        }
    }

    private fun onErrorRegisterFP(): (Throwable) -> Unit {
        return {
            it.printStackTrace()
            mutableRegisterFingerprintResult.postValue(Fail(it))
        }
    }

    fun unregisterFP(){
        fingerprintPreferenceHelper.unregisterFingerprint()
    }
}