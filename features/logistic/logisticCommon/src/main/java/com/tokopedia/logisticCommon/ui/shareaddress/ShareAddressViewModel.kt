package com.tokopedia.logisticCommon.ui.shareaddress

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.logisticCommon.domain.model.ShareAddressBottomSheetState
import com.tokopedia.logisticCommon.domain.request.RequestAddressParam
import com.tokopedia.logisticCommon.domain.request.ShareAddressParam
import com.tokopedia.logisticCommon.domain.response.ShareAddressResponse
import com.tokopedia.logisticCommon.domain.usecase.RequestAddressUseCase
import com.tokopedia.logisticCommon.domain.usecase.ShareAddressUseCase
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import javax.inject.Inject

class ShareAddressViewModel @Inject constructor(
    private val requestAddressUseCase: RequestAddressUseCase,
    private val shareAddressUseCase: ShareAddressUseCase,
    dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    private val mutableRequestAddressResponse = MutableLiveData<ShareAddressBottomSheetState<ShareAddressResponse>>()
    val requestAddressResponse: LiveData<ShareAddressBottomSheetState<ShareAddressResponse>>
        get() = mutableRequestAddressResponse

    private val mutableCheckShareAddressResponse = MutableLiveData<ShareAddressBottomSheetState<ShareAddressResponse>>()
    val checkShareAddressResponse: LiveData<ShareAddressBottomSheetState<ShareAddressResponse>>
        get() = mutableCheckShareAddressResponse

    fun requestShareAddress(param: RequestAddressParam) {
        launchCatchError(block = {
            showRequestAddressLoadingState(true)
            val result = requestAddressUseCase(param)
            showRequestAddressLoadingState(false)
            mutableRequestAddressResponse.value = if (result.shareAddressResponse.isSuccess) {
                ShareAddressBottomSheetState.Success(result)
            } else {
                ShareAddressBottomSheetState.Fail(result.shareAddressResponse.error)
            }
        }, onError = {
            showRequestAddressLoadingState(false)
            mutableRequestAddressResponse.value = ShareAddressBottomSheetState.Fail(it.message.orEmpty())
        })
    }

    private fun showRequestAddressLoadingState(isShowLoading: Boolean) {
        mutableRequestAddressResponse.value = ShareAddressBottomSheetState.Loading(isShowLoading)
    }

    fun checkShareAddress(param: ShareAddressParam) {
        launchCatchError(block = {
            showShareAddressLoadingState(true)
            val result = shareAddressUseCase(param)
            showShareAddressLoadingState(false)
            mutableCheckShareAddressResponse.value = if (result.shareAddressResponse.isSuccess) {
                ShareAddressBottomSheetState.Success(result)
            } else {
                ShareAddressBottomSheetState.Fail(result.shareAddressResponse.error)
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