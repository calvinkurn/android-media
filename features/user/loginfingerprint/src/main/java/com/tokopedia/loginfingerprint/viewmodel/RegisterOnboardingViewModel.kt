package com.tokopedia.loginfingerprint.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.loginfingerprint.data.model.RegisterFingerprintPojo
import com.tokopedia.loginfingerprint.data.model.RegisterFingerprintResult
import com.tokopedia.loginfingerprint.data.preference.FingerprintSetting
import com.tokopedia.loginfingerprint.domain.usecase.RegisterFingerprintUseCase
import com.tokopedia.loginfingerprint.utils.crypto.Cryptography
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.sessioncommon.ErrorHandlerSession
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created by Yoris Prayogo on 2020-02-05.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

class RegisterOnboardingViewModel @Inject constructor(dispatcher: CoroutineDispatchers,
                                                      private val userSession: UserSessionInterface,
                                                      private val cryptographyUtils: Cryptography?,
                                                      private val fingerprintSetting: FingerprintSetting,
                                                      private val registerFingerprintUseCase: RegisterFingerprintUseCase)
    : BaseViewModel(dispatcher.io){

    private val mutableRegisterFingerprintResult = MutableLiveData<Result<RegisterFingerprintResult>>()
    val verifyRegisterFingerprintResult: LiveData<Result<RegisterFingerprintResult>>
        get() = mutableRegisterFingerprintResult

    fun registerFingerprint(){
        val signature = cryptographyUtils?.generateFingerprintSignature(userSession.userId, userSession.deviceId)
        signature?.run {
            if(cryptographyUtils?.getPublicKey()?.isNotEmpty() == true){
                registerFingerprintUseCase.registerFingerprint(
                    this,
                    cryptographyUtils.getPublicKey(),
                    onSuccessRegisterFP(),
                    onErrorRegisterFP()
                )
            }else {
                onErrorRegisterFP().invoke(com.tokopedia.network.exception.MessageErrorException())
            }
        }
    }

    private fun onSuccessRegisterFP(): (RegisterFingerprintPojo) -> Unit {
        return {
            val response = it.data
            if (response.errorMessage.isBlank() && response.success) {
                mutableRegisterFingerprintResult.value = Success(it.data)
                cacheFingerprint()
            } else if (response.errorMessage.isNotBlank()) {
                mutableRegisterFingerprintResult.value = Fail(com.tokopedia.network.exception.MessageErrorException(response.errorMessage,
                        ErrorHandlerSession.ErrorCode.WS_ERROR.toString()))
            } else {
                mutableRegisterFingerprintResult.value = Fail(RuntimeException())
            }
        }
    }

    private fun cacheFingerprint() {
        fingerprintSetting.registerFingerprint()
        fingerprintSetting.saveUserId(userSession.userId)
    }

    private fun onErrorRegisterFP(): (Throwable) -> Unit {
        return {
            it.printStackTrace()
            mutableRegisterFingerprintResult.postValue(Fail(it))
        }
    }

    fun unregisterFP(){
        fingerprintSetting.unregisterFingerprint()
    }
}