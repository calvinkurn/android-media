package com.tokopedia.loginfingerprint.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.loginfingerprint.data.model.CheckFingerprintPojo
import com.tokopedia.loginfingerprint.domain.usecase.CheckFingerprintToggleStatusUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class SettingFingerprintViewModel @Inject constructor(dispatcher: CoroutineDispatchers,
                                                      private val userSession: UserSessionInterface,
                                                      private val checkFingerprintToggleStatusUseCase: CheckFingerprintToggleStatusUseCase)
    : BaseViewModel(dispatcher.main) {

    private val mutableCheckFingerprintStatus = MutableLiveData<Result<CheckFingerprintPojo>>()
    val checkFingerprintStatus: LiveData<Result<CheckFingerprintPojo>>
        get() = mutableCheckFingerprintStatus

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
}