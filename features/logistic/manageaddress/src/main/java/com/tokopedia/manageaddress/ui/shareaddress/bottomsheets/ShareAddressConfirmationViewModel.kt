package com.tokopedia.manageaddress.ui.shareaddress.bottomsheets

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.manageaddress.data.analytics.ShareAddressAnalytics
import com.tokopedia.manageaddress.domain.request.shareaddress.SelectShareAddressParam
import com.tokopedia.manageaddress.domain.request.shareaddress.ShareAddressToUserParam
import com.tokopedia.manageaddress.domain.usecase.shareaddress.SelectShareAddressUseCase
import com.tokopedia.manageaddress.domain.usecase.shareaddress.ShareAddressToUserUseCase
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import javax.inject.Inject

class ShareAddressConfirmationViewModel @Inject constructor(
    private val shareAddressToUserUseCase: ShareAddressToUserUseCase,
    private val selectShareAddressUseCase: SelectShareAddressUseCase,
    dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    private val _dismissEvent = SingleLiveEvent<Unit>()
    val dismissEvent: LiveData<Unit>
        get() = _dismissEvent

    private val _toastEvent = SingleLiveEvent<Toast>()
    val toastEvent: LiveData<Toast>
        get() = _toastEvent

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean>
        get() = _loading

    var isApprove = false

    fun shareAddress(param: ShareAddressToUserParam) {
        launchCatchError(block = {
            _loading.value = true
            val result = shareAddressToUserUseCase(param)
            _loading.value = false
            if (result.isSuccessShareAddress) {
                _toastEvent.value = Toast.Success
            } else {
                _toastEvent.value = Toast.Error(result.errorMessage)
            }
            ShareAddressAnalytics.directShareAgreeSendAddress(result.isSuccessShareAddress)
            _dismissEvent.call()
        }, onError = {
                _loading.value = false
                _toastEvent.value = Toast.Error(it.message.orEmpty())
                ShareAddressAnalytics.directShareAgreeSendAddress(false)
            })
    }

    fun shareAddressFromNotif(param: SelectShareAddressParam) {
        launchCatchError(block = {
            _loading.value = true
            val result = selectShareAddressUseCase(param)
            _loading.value = false
            if (result.isSuccess) {
                _toastEvent.value = Toast.Success
            } else {
                if (param.param.approve) {
                    _toastEvent.value = Toast.Error(result.errorMessage)
                }
            }
            if (param.param.approve) {
                ShareAddressAnalytics.fromNotifAgreeSendAddress(result.isSuccess)
            } else {
                ShareAddressAnalytics.fromNotifDisagreeSendAddress(result.isSuccess)
            }
            _dismissEvent.call()
        }, onError = {
                _loading.value = false
                _toastEvent.value = Toast.Error(it.message.orEmpty())
                if (param.param.approve) {
                    ShareAddressAnalytics.fromNotifAgreeSendAddress(false)
                } else {
                    ShareAddressAnalytics.fromNotifDisagreeSendAddress(false)
                }
            })
    }

    sealed interface Toast {
        object Success : Toast
        data class Error(val msg: String) : Toast
    }
}
