package com.tokopedia.loginfingerprint.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.loginfingerprint.data.model.VerifyFingerprint
import com.tokopedia.loginfingerprint.data.model.VerifyFingerprintPojo
import com.tokopedia.loginfingerprint.domain.usecase.VerifyFingerprintUseCase
import com.tokopedia.loginfingerprint.utils.crypto.Cryptography
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.sessioncommon.ErrorHandlerSession
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class FingerprintLandingViewModel @Inject constructor(dispatcher: CoroutineDispatchers,
                                                      private val userSession: UserSessionInterface,
                                                      private val verifyFingerprintUseCase: VerifyFingerprintUseCase,
                                                      private val cryptographyUtils: Cryptography?)
    : BaseViewModel(dispatcher.main){

    private val mutableVerifyFingerprint = MutableLiveData<Result<VerifyFingerprint>>()
    val verifyFingerprint: LiveData<Result<VerifyFingerprint>>
        get() = mutableVerifyFingerprint

    fun verifyFingerprint() {
        val signature = cryptographyUtils?.generateFingerprintSignature(userSession.adsId, userSession.deviceId)
        signature?.run {
            verifyFingerprintUseCase.verifyFingerprint(this, onSuccessVerifyFP(), onErrorVerifyFP())
        }
    }

    private fun onSuccessVerifyFP(): (VerifyFingerprintPojo) -> Unit {
        return {
            val data = it.data
            if (data.errorMessage.isBlank() && data.isSuccess && data.validateToken.isNotEmpty()) {
                mutableVerifyFingerprint.postValue(Success(it.data))
            } else if (data.errorMessage.isNotBlank()) {
                mutableVerifyFingerprint.postValue(Fail(
                    MessageErrorException(data.errorMessage,
                    ErrorHandlerSession.ErrorCode.WS_ERROR.toString())
                ))
            } else {
                mutableVerifyFingerprint.postValue(Fail(RuntimeException()))
            }
        }
    }

    private fun onErrorVerifyFP(): (Throwable) -> Unit {
        return {
            it.printStackTrace()
            mutableVerifyFingerprint.value = Fail(it)
        }
    }

}