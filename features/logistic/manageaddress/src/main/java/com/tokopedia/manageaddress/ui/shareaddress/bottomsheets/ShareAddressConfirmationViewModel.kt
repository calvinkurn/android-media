package com.tokopedia.manageaddress.ui.shareaddress.bottomsheets

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.manageaddress.ui.uimodel.ShareAddressBottomSheetState
import com.tokopedia.manageaddress.domain.usecase.shareaddress.ShareAddressToUserUseCase
import com.tokopedia.manageaddress.domain.request.shareaddress.ShareAddressToUserParam
import com.tokopedia.manageaddress.domain.request.shareaddress.SelectShareAddressParam
import com.tokopedia.manageaddress.domain.usecase.shareaddress.SelectShareAddressUseCase
import javax.inject.Inject

class ShareAddressConfirmationViewModel @Inject constructor(
    private val shareAddressToUserUseCase: ShareAddressToUserUseCase,
    private val selectShareAddressUseCase: SelectShareAddressUseCase,
    dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    private val mutableShareAddressResponse = MutableLiveData<ShareAddressBottomSheetState>()
    val shareAddressResponse: LiveData<ShareAddressBottomSheetState>
        get() = mutableShareAddressResponse

    var isApprove = false

    fun shareAddress(param: ShareAddressToUserParam) {
        launchCatchError(block = {
            showLoadingState(true)
            val result = shareAddressToUserUseCase(param)
            showLoadingState(false)
            mutableShareAddressResponse.value = if (result.isSuccessShareAddress) {
                ShareAddressBottomSheetState.Success
            } else {
                ShareAddressBottomSheetState.Fail(result.errorMessage)
            }
        }, onError = {
            showLoadingState(false)
            mutableShareAddressResponse.value = ShareAddressBottomSheetState.Fail(it.message.orEmpty())
        })
    }

    fun shareAddressFromNotif(param: SelectShareAddressParam) {
        launchCatchError(block = {
            showLoadingState(true)
            val result = selectShareAddressUseCase(param)
            showLoadingState(false)
            mutableShareAddressResponse.value = if (result.isSuccess) {
                ShareAddressBottomSheetState.Success
            } else {
                ShareAddressBottomSheetState.Fail(result.errorMessage)
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
