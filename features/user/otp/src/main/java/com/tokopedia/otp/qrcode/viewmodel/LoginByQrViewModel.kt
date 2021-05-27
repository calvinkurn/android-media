package com.tokopedia.otp.qrcode.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.otp.notif.domain.pojo.ChangeStatusPushNotifData
import com.tokopedia.otp.notif.view.fragment.ReceiverNotifFragment.Companion.STATUS_APPROVE
import com.tokopedia.otp.qrcode.domain.pojo.VerifyQrData
import com.tokopedia.otp.qrcode.domain.usecase.VerifyQrUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class LoginByQrViewModel @Inject constructor(
        private val verifyQrUseCase: VerifyQrUseCase,
        dispatcherProvider: CoroutineDispatchers
) : BaseViewModel(dispatcherProvider.main) {

    private val _verifyQrResult = MutableLiveData<Result<VerifyQrData>>()
    val verifyQrResult: LiveData<Result<VerifyQrData>>
        get() = _verifyQrResult

    fun verifyQrCode(
            uuid: String,
            status: String,
            signature: String
    ) {
        launchCatchError(coroutineContext, {
            val params = verifyQrUseCase.getParams(uuid, status, signature)
            val data = verifyQrUseCase.getData(params).data
            data.approvalStatus = status
            when {
                data.imglink.isNotEmpty() &&
                        data.messageTitle.isNotEmpty() &&
                        data.messageBody.isNotEmpty() &&
                        data.buttonType.isNotEmpty() -> {
                    _verifyQrResult.value = Success(data)
                }
                data.errorMessage.isNotEmpty() -> {
                    _verifyQrResult.postValue(Fail(MessageErrorException(data.errorMessage)))
                }
                data.message.isNotEmpty() -> {
                    _verifyQrResult.postValue(Fail(MessageErrorException(data.message)))
                }
                else -> {
                    _verifyQrResult.postValue(Fail(Throwable()))
                }
            }
        }, {
            _verifyQrResult.postValue(Fail(Throwable()))
        })
    }

}