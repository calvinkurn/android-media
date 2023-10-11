package com.tokopedia.manageaddress.ui.shareaddress.bottomsheets

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.manageaddress.data.analytics.ShareAddressAnalytics
import com.tokopedia.manageaddress.domain.request.shareaddress.SelectShareAddressParam
import com.tokopedia.manageaddress.domain.request.shareaddress.ShareAddressToUserParam
import com.tokopedia.manageaddress.domain.usecase.shareaddress.SelectShareAddressUseCase
import com.tokopedia.manageaddress.domain.usecase.shareaddress.ShareAddressToUserUseCase
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import kotlinx.coroutines.launch
import javax.inject.Inject

class ShareAddressConfirmationViewModel @Inject constructor(
    private val shareAddressToUserUseCase: ShareAddressToUserUseCase,
    private val selectShareAddressUseCase: SelectShareAddressUseCase,
    dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    private val _dismissEvent = SingleLiveEvent<Unit>()
    val dismissEvent: LiveData<Unit>
        get() = _dismissEvent

    private val _leavePageEvent = SingleLiveEvent<Unit>()
    val leavePageEvent: LiveData<Unit>
        get() = _leavePageEvent

    private val _toastEvent = SingleLiveEvent<Toast>()
    val toastEvent: LiveData<Toast>
        get() = _toastEvent

    private val _loading: MutableLiveData<LoadingState> = MutableLiveData(LoadingState.NotLoading)
    val loading: LiveData<LoadingState>
        get() = _loading

    fun shareAddress(param: ShareAddressToUserParam) {
        launch {
            try {
                _loading.value = LoadingState.AgreeLoading
                val result = shareAddressToUserUseCase(param)
                _toastEvent.value =
                    if (result.isSuccessShareAddress) Toast.Success else Toast.Error(result.errorMessage)
                ShareAddressAnalytics.directShareAgreeSendAddress(result.isSuccessShareAddress)
            } catch (e: Exception) {
                _toastEvent.value = Toast.Error(e.message.orEmpty())
                ShareAddressAnalytics.directShareAgreeSendAddress(false)
            }
            _loading.value = LoadingState.NotLoading
            _dismissEvent.call()
        }
    }

    fun shareAddressFromNotif(param: SelectShareAddressParam.Param) {
        launch {
            try {
                _loading.value =
                    if (param.approve) LoadingState.AgreeLoading else LoadingState.DisagreeLoading
                val result = selectShareAddressUseCase(param)
                if (param.approve) {
                    ShareAddressAnalytics.fromNotifAgreeSendAddress(result.isSuccess)
                    if (result.isSuccess) {
                        _toastEvent.value = Toast.Success
                        _leavePageEvent.call()
                    } else {
                        _toastEvent.value = Toast.Error(result.errorMessage)
                    }
                } else {
                    ShareAddressAnalytics.fromNotifDisagreeSendAddress(result.isSuccess)
                }
            } catch (e: Exception) {
                _toastEvent.value = Toast.Error(e.message.orEmpty())
                if (param.approve) {
                    ShareAddressAnalytics.fromNotifAgreeSendAddress(false)
                } else {
                    ShareAddressAnalytics.fromNotifDisagreeSendAddress(false)
                }
            }
            _loading.value = LoadingState.NotLoading
            _dismissEvent.call()
        }
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
