package com.tokopedia.otp.notif.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.otp.notif.domain.pojo.ChangeStatusPushNotifData
import com.tokopedia.otp.notif.domain.pojo.DeviceStatusPushNotifData
import com.tokopedia.otp.notif.domain.pojo.VerifyPushNotifData
import com.tokopedia.otp.notif.domain.pojo.VerifyPushNotifExpData
import com.tokopedia.otp.notif.domain.usecase.ChangeStatusPushNotifUseCase
import com.tokopedia.otp.notif.domain.usecase.DeviceStatusPushNotifUseCase
import com.tokopedia.otp.notif.domain.usecase.VerifyPushNotifExpUseCase
import com.tokopedia.otp.notif.domain.usecase.VerifyPushNotifUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class NotifViewModel @Inject constructor(
        private val changeStatusPushNotifUseCase: ChangeStatusPushNotifUseCase,
        private val deviceStatusPushNotifUseCase: DeviceStatusPushNotifUseCase,
        private val verifyPushNotifUseCase: VerifyPushNotifUseCase,
        private val verifyPushNotifExpUseCase: VerifyPushNotifExpUseCase,
        dispatcherProvider: CoroutineDispatchers
) : BaseViewModel(dispatcherProvider.main) {

    private val _changeStatusPushNotifResult = MutableLiveData<Result<ChangeStatusPushNotifData>>()
    val changeStatusPushNotifResult: LiveData<Result<ChangeStatusPushNotifData>>
        get() = _changeStatusPushNotifResult

    private val _deviceStatusPushNotifResult = MutableLiveData<Result<DeviceStatusPushNotifData>>()
    val deviceStatusPushNotifResult: LiveData<Result<DeviceStatusPushNotifData>>
        get() = _deviceStatusPushNotifResult

    private val _verifyPushNotifResult = MutableLiveData<Result<VerifyPushNotifData>>()
    val verifyPushNotifResult: LiveData<Result<VerifyPushNotifData>>
        get() = _verifyPushNotifResult

    private val _verifyPushNotifExpResult = MutableLiveData<Result<VerifyPushNotifExpData>>()
    val verifyPushNotifExpResult: LiveData<Result<VerifyPushNotifExpData>>
        get() = _verifyPushNotifExpResult

    fun changeStatusPushNotif(status: Boolean) {
        launchCatchError(coroutineContext, {
            val params = changeStatusPushNotifUseCase.getParams(if (status) 1 else 0)
            val data = changeStatusPushNotifUseCase.getData(params).data
            when {
                data.success -> {
                    data.isChecked = status
                    _changeStatusPushNotifResult.value = Success(data)
                }
                data.errorMessage.isNotEmpty() -> {
                    _changeStatusPushNotifResult.postValue(Fail(MessageErrorException(data.errorMessage)))
                }
                else -> {
                    _changeStatusPushNotifResult.postValue(Fail(Throwable()))
                }
            }
        }, {
            _changeStatusPushNotifResult.postValue(Fail(it))
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
                data.imglink.isNotEmpty() &&
                data.messageTitle.isNotEmpty() &&
                data.messageBody.isNotEmpty() &&
                data.ctaType.isNotEmpty() -> {
                    _verifyPushNotifResult.value = Success(data)
                }
                data.errorMessage.isNotEmpty() -> {
                    _verifyPushNotifResult.postValue(Fail(MessageErrorException(data.errorMessage)))
                }
                data.message.isNotEmpty() -> {
                    _verifyPushNotifResult.postValue(Fail(MessageErrorException(data.message)))
                }
                else -> {
                    _verifyPushNotifResult.postValue(Fail(Throwable()))
                }
            }
        }, {
            _verifyPushNotifResult.postValue(Fail(it))
        })
    }

    fun verifyPushNotifExp(
            challengeCode: String,
            signature: String,
            status: String
    ) {
        launchCatchError(coroutineContext, {
            val params = verifyPushNotifExpUseCase.getParams(challengeCode, signature, status)
            val data = verifyPushNotifExpUseCase.getData(params).data
            when {
                data.errorMessage.isNotEmpty() -> {
                    _verifyPushNotifExpResult.postValue(Fail(MessageErrorException(data.errorMessage)))
                }
                else -> {
                    _verifyPushNotifExpResult.value = Success(data)
                }
            }
        }, {
            _verifyPushNotifExpResult.postValue(Fail(it))
        })
    }
}