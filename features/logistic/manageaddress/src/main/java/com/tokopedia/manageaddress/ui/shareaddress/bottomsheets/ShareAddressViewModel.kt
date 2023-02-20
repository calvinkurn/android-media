package com.tokopedia.manageaddress.ui.shareaddress.bottomsheets

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.manageaddress.ui.uimodel.ShareAddressBottomSheetState
import com.tokopedia.manageaddress.domain.request.shareaddress.SendShareAddressRequestParam
import com.tokopedia.manageaddress.domain.request.shareaddress.ShareAddressToUserParam
import com.tokopedia.manageaddress.domain.usecase.shareaddress.SendShareAddressRequestUseCase
import com.tokopedia.manageaddress.domain.usecase.shareaddress.ShareAddressToUserUseCase
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
            showLoadingRequestAddress(true)
            val result = sendShareAddressRequestUseCase(param)
            showLoadingRequestAddress(false)
            mutableRequestAddressResponse.value = if (result.isSuccess) {
                ShareAddressBottomSheetState.Success
            } else {
                ShareAddressBottomSheetState.Fail(result.errorMessage)
            }
        }, onError = {
            showLoadingRequestAddress(false)
            mutableRequestAddressResponse.value = ShareAddressBottomSheetState.Fail(it.message.orEmpty())
        })
    }

    private fun showLoadingRequestAddress(isShowLoading: Boolean) {
        mutableRequestAddressResponse.value = ShareAddressBottomSheetState.Loading(isShowLoading)
    }

    fun checkShareAddress(param: ShareAddressToUserParam) {
        launchCatchError(block = {
            showLoadingCheckAddress(true)
            val result = shareAddressToUserUseCase(param)
            showLoadingCheckAddress(false)
            mutableCheckShareAddressResponse.value = if (result.isSuccessInitialCheck) {
                ShareAddressBottomSheetState.Success
            } else {
                ShareAddressBottomSheetState.Fail(result.errorMessage)
            }
        }, onError = {
            showLoadingCheckAddress(false)
            mutableCheckShareAddressResponse.value = ShareAddressBottomSheetState.Fail(it.message.orEmpty())
        })
    }

    private fun showLoadingCheckAddress(isShow: Boolean) {
        mutableCheckShareAddressResponse.value = ShareAddressBottomSheetState.Loading(isShow)
    }
}
