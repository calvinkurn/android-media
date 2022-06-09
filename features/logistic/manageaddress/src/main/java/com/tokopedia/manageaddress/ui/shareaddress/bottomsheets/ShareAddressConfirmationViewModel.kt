package com.tokopedia.manageaddress.ui.shareaddress.bottomsheets

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.logisticCommon.domain.response.ShareAddressResponse
import com.tokopedia.manageaddress.domain.ShareAddressUseCase
import com.tokopedia.manageaddress.domain.model.shareaddress.ShareAddressParam
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class ShareAddressConfirmationViewModel @Inject constructor(
    private val shareAddressUseCase: ShareAddressUseCase,
    dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    private val mutableShareAddressResponse = MutableLiveData<Result<ShareAddressResponse>>()
    val shareAddressResponse: LiveData<Result<ShareAddressResponse>>
        get() = mutableShareAddressResponse

    fun shareAddress(param: ShareAddressParam) {
        launchCatchError(block = {
            val result = shareAddressUseCase(param)
            mutableShareAddressResponse.value = Success(result)
        }, onError = {
            mutableShareAddressResponse.value = Fail(it)
        })
    }
}