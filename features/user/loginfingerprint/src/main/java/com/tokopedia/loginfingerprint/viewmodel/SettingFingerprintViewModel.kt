package com.tokopedia.loginfingerprint.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.loginfingerprint.data.model.CheckFingerprintPojo
import com.tokopedia.loginfingerprint.data.model.RegisterFingerprintPojo
import com.tokopedia.loginfingerprint.data.model.RegisterFingerprintResult
import com.tokopedia.loginfingerprint.domain.usecase.CheckFingerprintToggleStatusUseCase
import com.tokopedia.loginfingerprint.domain.usecase.RegisterFingerprintUseCase
import com.tokopedia.loginfingerprint.domain.usecase.VerifyFingerprintUseCase
import com.tokopedia.loginfingerprint.utils.crypto.Cryptography
import com.tokopedia.sessioncommon.ErrorHandlerSession
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class SettingFingerprintViewModel @Inject constructor(dispatcher: CoroutineDispatchers,
                                                      private val userSession: UserSessionInterface,
                                                      private val verifyFingerprintUseCase: VerifyFingerprintUseCase,
                                                      private val registerFingerprintUseCase: RegisterFingerprintUseCase,
                                                      private val cryptographyUtils: Cryptography?,
                                                      private val checkFingerprintToggleStatusUseCase: CheckFingerprintToggleStatusUseCase)
    : BaseViewModel(dispatcher.main) {

    private val mutableCheckFingerprintStatus = MutableLiveData<Result<CheckFingerprintPojo>>()
    val checkFingerprintStatus: LiveData<Result<CheckFingerprintPojo>>
        get() = mutableCheckFingerprintStatus

    private val mutableRegisterFingerprintResult = MutableLiveData<Result<RegisterFingerprintResult>>()
    val registerFingerprintResult: LiveData<Result<RegisterFingerprintResult>>
        get() = mutableRegisterFingerprintResult

    fun getFingerprintStatus() {
        checkFingerprintToggleStatusUseCase.checkFingerprint(userSession.userId,
            onSuccess = {
                if(it.data.isSuccess && it.data.errorMessage.isEmpty()) {
                    mutableCheckFingerprintStatus.postValue(Success(it))
                } else {
                    mutableCheckFingerprintStatus.postValue(Fail(Throwable("Gagal")))
                }
        }, onError = {
            mutableCheckFingerprintStatus.postValue(Fail(it))
        })
    }

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
            } else if (response.errorMessage.isNotBlank()) {
                mutableRegisterFingerprintResult.value = Fail(com.tokopedia.network.exception.MessageErrorException(response.errorMessage,
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

}