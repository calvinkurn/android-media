package com.tokopedia.loginfingerprint.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.loginfingerprint.constant.BiometricConstant
import com.tokopedia.loginfingerprint.data.model.CheckFingerprintResult
import com.tokopedia.loginfingerprint.data.model.RegisterFingerprintPojo
import com.tokopedia.loginfingerprint.data.model.RegisterFingerprintResult
import com.tokopedia.loginfingerprint.data.model.RemoveFingerprintData
import com.tokopedia.loginfingerprint.di.LoginFingerprintQueryConstant
import com.tokopedia.loginfingerprint.domain.usecase.CheckFingerprintToggleStatusUseCase
import com.tokopedia.loginfingerprint.domain.usecase.RegisterFingerprintUseCase
import com.tokopedia.loginfingerprint.domain.usecase.RemoveFingerprintUsecase
import com.tokopedia.loginfingerprint.utils.crypto.Cryptography
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.sessioncommon.data.fingerprint.FingerprintPreference
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SettingFingerprintViewModel @Inject constructor(val dispatcher: CoroutineDispatchers,
                                                      private val userSession: UserSessionInterface,
                                                      private val registerFingerprintUseCase: RegisterFingerprintUseCase,
                                                      private val removeFingerprintUseCase: RemoveFingerprintUsecase,
                                                      private val cryptographyUtils: Cryptography?,
                                                      private val checkFingerprintToggleStatusUseCase: CheckFingerprintToggleStatusUseCase,
                                                      private val fingerprintPreference: FingerprintPreference)
    : BaseViewModel(dispatcher.main) {

    private val mutableCheckFingerprintStatus = MutableLiveData<Result<CheckFingerprintResult>>()
    val checkFingerprintStatus: LiveData<Result<CheckFingerprintResult>>
        get() = mutableCheckFingerprintStatus

    private val mutableRegisterFingerprintResult = MutableLiveData<Result<RegisterFingerprintResult>>()
    val registerFingerprintResult: LiveData<Result<RegisterFingerprintResult>>
        get() = mutableRegisterFingerprintResult

    private val mutableRemoveFingerprintResult = MutableLiveData<Result<RemoveFingerprintData>>()
    val removeFingerprintResult: LiveData<Result<RemoveFingerprintData>>
        get() = mutableRemoveFingerprintResult

    private val mutableRegisterResultEvent = SingleLiveEvent<Boolean>()
    val registerResultEvent: LiveData<Boolean> = mutableRegisterResultEvent

    private val mutableRegisterErrorEvent = SingleLiveEvent<String>()
    val registerErrorEvent: LiveData<String> = mutableRegisterErrorEvent

    fun getFingerprintStatus() {
        launchCatchError(block = {
            val result = checkFingerprintToggleStatusUseCase(userSession.userId).data
            if(result.isSuccess && result.errorMessage.isEmpty()) {
                mutableCheckFingerprintStatus.value = Success(result)
            } else {
                mutableCheckFingerprintStatus.value = Fail(Throwable("Gagal"))
            }
        },
        onError = {
            mutableCheckFingerprintStatus.value = Fail(it)
        })
    }

    fun registerFingerprint(){
        launchCatchError(block = {
            val signatureModel = cryptographyUtils?.generateFingerprintSignature(userSession.userId, userSession.deviceId)
            if(signatureModel != null) {
                val publicKey = cryptographyUtils?.getPublicKey() ?: ""
                if(publicKey.isNotEmpty() && signatureModel.signature.isNotEmpty()) {
                    val params = mapOf(
                        LoginFingerprintQueryConstant.PARAM_PUBLIC_KEY to publicKey,
                        LoginFingerprintQueryConstant.PARAM_SIGNATURE to signatureModel.signature,
                        LoginFingerprintQueryConstant.PARAM_DATETIME to signatureModel.datetime,
                        BiometricConstant.PARAM_BIOMETRIC_ID to fingerprintPreference.getOrCreateUniqueId()
                    )
                    val result = registerFingerprintUseCase(params)
                    withContext(dispatcher.main) {
                        onSuccessRegisterFP(result)
                    }
                } else {
                    mutableRegisterResultEvent.value = false
                    mutableRegisterErrorEvent.value = "Terjadi Kesalahan, Silahkan coba lagi"
                }
            }
        }, onError = {
            mutableRegisterResultEvent.value = false
            mutableRegisterErrorEvent.value = it.message
        })
    }

    fun removeFingerprint() {
        launchCatchError(block = {
            val result = removeFingerprintUseCase(Unit)
            if(result.data.isSuccess && result.data.error.isEmpty()) {
                fingerprintPreference.removeUniqueId()
                mutableRemoveFingerprintResult.value = Success(result.data)
            } else {
                mutableRemoveFingerprintResult.value = Fail(MessageErrorException(result.data.error))
            }
        }, onError = {
            mutableRemoveFingerprintResult.value = Fail(it)
        })
    }

    private fun onSuccessRegisterFP(registerFingerprintPojo: RegisterFingerprintPojo) {
        val response = registerFingerprintPojo.data
        if (response.errorMessage.isBlank() && response.success) {
            mutableRegisterResultEvent.value = true
        } else if (response.errorMessage.isNotBlank()) {
            mutableRegisterResultEvent.value = false
            mutableRegisterErrorEvent.value = response.errorMessage
        } else {
            mutableRegisterResultEvent.value = false
            mutableRegisterErrorEvent.value = response.errorMessage
        }
    }

}