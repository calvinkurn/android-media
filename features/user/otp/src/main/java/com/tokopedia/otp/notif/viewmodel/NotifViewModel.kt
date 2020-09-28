package com.tokopedia.otp.notif.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.otp.common.DispatcherProvider
import com.tokopedia.otp.notif.domain.pojo.ChangeOtpPushNotifData
import com.tokopedia.otp.notif.domain.pojo.DeviceStatusPushNotifData
import com.tokopedia.otp.notif.domain.pojo.VerifyPushNotifData
import com.tokopedia.otp.notif.domain.usecase.ChangeOtpPushNotifUseCase
import com.tokopedia.otp.notif.domain.usecase.DeviceStatusPushNotifUseCase
import com.tokopedia.otp.notif.domain.usecase.VerifyPushNotifUseCase
import com.tokopedia.otp.notif.view.fragment.RecieverNotifFragment
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class NotifViewModel @Inject constructor(
        private val changeOtpPushNotifUseCase: ChangeOtpPushNotifUseCase,
        private val deviceStatusPushNotifUseCase: DeviceStatusPushNotifUseCase,
        private val verifyPushNotifUseCase: VerifyPushNotifUseCase,
        dispatcherProvider: DispatcherProvider
) : BaseViewModel(dispatcherProvider.ui()) {

    private val _changeOtpPushNotifResult = MutableLiveData<Result<ChangeOtpPushNotifData>>()
    val changeOtpPushNotifResult: LiveData<Result<ChangeOtpPushNotifData>>
        get() = _changeOtpPushNotifResult

    private val _deviceStatusPushNotifResult = MutableLiveData<Result<DeviceStatusPushNotifData>>()
    val deviceStatusPushNotifResult: LiveData<Result<DeviceStatusPushNotifData>>
        get() = _deviceStatusPushNotifResult

    private val _verifyPushNotifResult = MutableLiveData<Result<VerifyPushNotifData>>()
    val verifyPushNotifResult: LiveData<Result<VerifyPushNotifData>>
        get() = _verifyPushNotifResult

    fun changeOtpPushNotif(status: Int) {
        launchCatchError(coroutineContext, {
            val params = changeOtpPushNotifUseCase.getParams(status)
            val data = changeOtpPushNotifUseCase.getData(params).data
            when {
                data.success -> {
                    _changeOtpPushNotifResult.value = Success(data)
                }
                data.errorMessage.isNotEmpty() -> {
                    _changeOtpPushNotifResult.postValue(Fail(MessageErrorException(data.errorMessage)))
                }
                else -> {
                    _changeOtpPushNotifResult.postValue(Fail(Throwable()))
                }
            }
        }, {
            _changeOtpPushNotifResult.postValue(Fail(it))
        })
    }

    fun deviceStatusPushNotif() {
        launchCatchError(coroutineContext, {
            val data = deviceStatusPushNotifUseCase.getData(mapOf()).data
            when {
                data.success -> {
                    _deviceStatusPushNotifResult.value = Success(data)
                }
                data.errorMessage.isNotEmpty() -> {
                    _deviceStatusPushNotifResult.postValue(Fail(MessageErrorException(data.errorMessage)))
                }
                else -> {
                    _deviceStatusPushNotifResult.postValue(Fail(Throwable()))
                }
            }
        }, {
            _deviceStatusPushNotifResult.postValue(Fail(it))
        })
    }

    fun verifyPushNotif(
            challengeCode: String,
            signature: String,
            status: String
    ) {
        launchCatchError(coroutineContext, {
            val params = verifyPushNotifUseCase.getParams(challengeCode, signature, status)
            val data = verifyPushNotifUseCase.getData(params).data
            when {
                data.success -> {
                    _verifyPushNotifResult.value = Success(data)
                }
                data.errorMessage.isNotEmpty() -> {
                    _verifyPushNotifResult.postValue(Fail(MessageErrorException(data.errorMessage)))
                }
                else -> {
                    _verifyPushNotifResult.postValue(Fail(Throwable()))
                }
            }
        }, {
            _verifyPushNotifResult.postValue(Fail(it))
        })
    }
}