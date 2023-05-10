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

    private val _loading: MutableLiveData<LoadingState> = MutableLiveData(LoadingState.NotLoading)
    val loading: LiveData<LoadingState>
        get() = _loading

    fun shareAddress(param: ShareAddressToUserParam) {
        launchCatchError(
            block = {
                _loading.value = LoadingState.AgreeLoading
                val result = shareAddressToUserUseCase(param)
                if (result.isSuccessShareAddress) {
                    _toastEvent.value = Toast.Success
                } else {
                    _toastEvent.value = Toast.Error(result.errorMessage)
                }
                ShareAddressAnalytics.directShareAgreeSendAddress(result.isSuccessShareAddress)
                _loading.value = LoadingState.NotLoading
                _dismissEvent.call()
            },
            onError = {
                _loading.value = LoadingState.NotLoading
                _dismissEvent.call()
                _toastEvent.value = Toast.Error(it.message.orEmpty())
                ShareAddressAnalytics.directShareAgreeSendAddress(false)
            }
        )
    }

    fun shareAddressFromNotif(param: SelectShareAddressParam.Param) {
        launchCatchError(
            block = {
                if (param.approve) {
                    _loading.value = LoadingState.AgreeLoading
                } else {
                    _loading.value = LoadingState.DisagreeLoading
                }
                val result = selectShareAddressUseCase(param)
                if (result.isSuccess) {
                    _toastEvent.value = Toast.Success
                } else {
                    if (param.approve) {
                        _toastEvent.value = Toast.Error(result.errorMessage)
                    }
                }
                if (param.approve) {
                    ShareAddressAnalytics.fromNotifAgreeSendAddress(result.isSuccess)
                } else {
                    ShareAddressAnalytics.fromNotifDisagreeSendAddress(result.isSuccess)
                }
                _loading.value = LoadingState.NotLoading
                _dismissEvent.call()
            },
            onError = {
                _loading.value = LoadingState.NotLoading
                _toastEvent.value = Toast.Error(it.message.orEmpty())
                _dismissEvent.call()
                if (param.approve) {
                    ShareAddressAnalytics.fromNotifAgreeSendAddress(false)
                } else {
                    ShareAddressAnalytics.fromNotifDisagreeSendAddress(false)
                }
            }
        )
    }

    sealed interface Toast {
        object Success : Toast
        data class Error(val msg: String) : Toast
    }

    sealed interface LoadingState {
        object NotLoading : LoadingState
        object AgreeLoading : LoadingState
        object DisagreeLoading : LoadingState
    }
}
