package com.tokopedia.manageaddress.ui.shareaddress.bottomsheets

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.logisticCommon.domain.model.ShareAddressBottomSheetState
import com.tokopedia.logisticCommon.domain.response.ShareAddressResponse
import com.tokopedia.logisticCommon.domain.usecase.ShareAddressUseCase
import com.tokopedia.logisticCommon.domain.request.ShareAddressParam
import javax.inject.Inject

class ShareAddressConfirmationViewModel @Inject constructor(
    private val shareAddressUseCase: ShareAddressUseCase,
    dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    private val mutableShareAddressResponse = MutableLiveData<ShareAddressBottomSheetState<ShareAddressResponse>>()
    val shareAddressResponse: LiveData<ShareAddressBottomSheetState<ShareAddressResponse>>
        get() = mutableShareAddressResponse

    fun shareAddress(param: ShareAddressParam) {
        launchCatchError(block = {
            showLoadingState(true)
            val result = shareAddressUseCase(param)
            showLoadingState(false)
            mutableShareAddressResponse.value = if (result.shareAddressResponse.isSuccess) {
                ShareAddressBottomSheetState.Success(result)
            } else {
                ShareAddressBottomSheetState.Fail(result.shareAddressResponse.error)
            }
        }, onError = {
            showLoadingState(false)
            mutableShareAddressResponse.value = ShareAddressBottomSheetState.Fail(it.message.orEmpty())
        })
    }

    private fun showLoadingState(isShowLoading: Boolean) {
        mutableShareAddressResponse.value = ShareAddressBottomSheetState.Loading(isShowLoading)
    }
}