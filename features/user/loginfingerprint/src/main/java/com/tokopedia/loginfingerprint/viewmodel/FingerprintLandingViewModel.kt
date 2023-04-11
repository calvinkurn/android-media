package com.tokopedia.loginfingerprint.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.loginfingerprint.data.model.VerifyFingerprint
import com.tokopedia.loginfingerprint.domain.usecase.VerifyFingerprintUseCase
import com.tokopedia.loginfingerprint.utils.crypto.KeyPairManager
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.sessioncommon.ErrorHandlerSession
import com.tokopedia.sessioncommon.data.fingerprint.FingerprintPreference
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import dagger.Lazy
import javax.inject.Inject

class FingerprintLandingViewModel @Inject constructor(dispatcher: CoroutineDispatchers,
                                                      private val userSession: UserSessionInterface,
                                                      private val keyPairManager: Lazy<KeyPairManager?>,
                                                      private val verifyFingerprintUseCase: VerifyFingerprintUseCase,
                                                      private val fingerprintPreference: FingerprintPreference)
    : BaseViewModel(dispatcher.main){

    private val mutableVerifyFingerprint = MutableLiveData<Result<VerifyFingerprint>>()
    val verifyFingerprint: LiveData<Result<VerifyFingerprint>>
        get() = mutableVerifyFingerprint

    fun verifyFingerprint() {
        launchCatchError(block = {
            val signature = keyPairManager.get()?.generateFingerprintSignature(fingerprintPreference.getUniqueId(), userSession.deviceId)
            if(signature != null) {
                val result = verifyFingerprintUseCase(signature)
                onSuccessVerifyFP(result.data)
            }
        }, onError = {
            mutableVerifyFingerprint.value = Fail(it)
        })
    }

    private fun onSuccessVerifyFP(data: VerifyFingerprint) {
        if (data.errorMessage.isBlank() && data.isSuccess && data.validateToken.isNotEmpty()) {
            mutableVerifyFingerprint.value = Success(data)
        } else if (data.errorMessage.isNotBlank()) {
            mutableVerifyFingerprint.value = Fail(
                MessageErrorException(data.errorMessage,
                ErrorHandlerSession.ErrorCode.WS_ERROR.toString())
            )
        } else {
            mutableVerifyFingerprint.value = Fail(RuntimeException())
        }
    }

}
