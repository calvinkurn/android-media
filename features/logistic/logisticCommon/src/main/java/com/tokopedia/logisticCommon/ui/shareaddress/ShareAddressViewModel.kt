package com.tokopedia.logisticCommon.ui.shareaddress

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.logisticCommon.domain.model.ShareAddressBottomSheetState
import com.tokopedia.logisticCommon.domain.request.SendShareAddressRequestParam
import com.tokopedia.logisticCommon.domain.request.ShareAddressToUserParam
import com.tokopedia.logisticCommon.domain.usecase.SendShareAddressRequestUseCase
import com.tokopedia.logisticCommon.domain.usecase.ShareAddressToUserUseCase
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import javax.inject.Inject

class ShareAddressViewModel @Inject constructor(
    private val sendShareAddressRequestUseCase: SendShareAddressRequestUseCase,
    private val shareAddressToUserUseCase: ShareAddressToUserUseCase,
    dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    private val mutableRequestAddressResponse = MutableLiveData<ShareAddressBottomSheetState>()
    val requestAddressResponse: LiveData<ShareAddressBottomSheetState>
        get() = mutableRequestAddressResponse

    private val mutableCheckShareAddressResponse = MutableLiveData<ShareAddressBottomSheetState>()
    val checkShareAddressResponse: LiveData<ShareAddressBottomSheetState>
        get() = mutableCheckShareAddressResponse

    fun requestShareAddress(param: SendShareAddressRequestParam) {
        launchCatchError(block = {
            showRequestAddressLoadingState(true)
            val result = sendShareAddressRequestUseCase(param)
            showRequestAddressLoadingState(false)
            mutableRequestAddressResponse.value = if (result.isSuccess) {
                ShareAddressBottomSheetState.Success
            } else {
                ShareAddressBottomSheetState.Fail(result.errorMessage)
            }
        }, onError = {
            showRequestAddressLoadingState(false)
            mutableRequestAddressResponse.value = ShareAddressBottomSheetState.Fail(it.message.orEmpty())
        })
    }

    private fun showRequestAddressLoadingState(isShowLoading: Boolean) {
        mutableRequestAddressResponse.value = ShareAddressBottomSheetState.Loading(isShowLoading)
    }

    fun checkShareAddress(param: ShareAddressToUserParam) {
        launchCatchError(block = {
            showShareAddressLoadingState(true)
            val result = shareAddressToUserUseCase(param)
            showShareAddressLoadingState(false)
            mutableCheckShareAddressResponse.value = if (result.isSuccessInitialCheck) {
                ShareAddressBottomSheetState.Success
            } else {
                ShareAddressBottomSheetState.Fail(result.errorMessage)
            }
        }, onError = {
            showShareAddressLoadingState(false)
            mutableCheckShareAddressResponse.value = ShareAddressBottomSheetState.Fail(it.message.orEmpty())
        })
    }

    private fun showShareAddressLoadingState(isShowLoading: Boolean) {
        mutableCheckShareAddressResponse.value = ShareAddressBottomSheetState.Loading(isShowLoading)
    }
}