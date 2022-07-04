package com.tokopedia.logisticCommon.ui.shareaddress

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.logisticCommon.domain.model.ShareAddressBottomSheetState
import com.tokopedia.logisticCommon.domain.request.RequestShareAddress
import com.tokopedia.logisticCommon.domain.response.ShareAddressResponse
import com.tokopedia.logisticCommon.domain.usecase.RequestShareAddressUseCase
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import javax.inject.Inject

class ShareAddressViewModel @Inject constructor(
    private val requestShareAddressUseCase: RequestShareAddressUseCase,
    dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    private val mutableRequestAddressResponse = MutableLiveData<ShareAddressBottomSheetState<ShareAddressResponse>>()
    val requestAddressResponse: LiveData<ShareAddressBottomSheetState<ShareAddressResponse>>
        get() = mutableRequestAddressResponse

    fun requestShareAddress(param: RequestShareAddress) {
        launchCatchError(block = {
            showLoadingState(true)
            val result = requestShareAddressUseCase(param)
            showLoadingState(false)
            mutableRequestAddressResponse.value = if (result.shareAddressResponse.isSuccess) {
                ShareAddressBottomSheetState.Success(result)
            } else {
                ShareAddressBottomSheetState.Fail(result.shareAddressResponse.error)
            }
        }, onError = {
            showLoadingState(false)
            mutableRequestAddressResponse.value = ShareAddressBottomSheetState.Fail(it.message.orEmpty())
        })
    }

    private fun showLoadingState(isShowLoading: Boolean) {
        mutableRequestAddressResponse.value = ShareAddressBottomSheetState.Loading(isShowLoading)
    }

    fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}