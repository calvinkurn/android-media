package com.tokopedia.logisticCommon.ui.shareaddress

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.logisticCommon.domain.request.RequestShareAddress
import com.tokopedia.logisticCommon.domain.response.ShareAddressResponse
import com.tokopedia.logisticCommon.domain.usecase.RequestShareAddressUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import javax.inject.Inject

class ShareAddressViewModel @Inject constructor(
    private val requestShareAddressUseCase: RequestShareAddressUseCase,
    dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    private val mutableRequestShareAddressResponse = MutableLiveData<Result<ShareAddressResponse>>()
    val requestShareAddressResponse: LiveData<Result<ShareAddressResponse>>
        get() = mutableRequestShareAddressResponse

    fun requestShareAddress(param: RequestShareAddress) {
        launchCatchError(block = {
            val result = requestShareAddressUseCase(param)
            mutableRequestShareAddressResponse.value = Success(result)
        }, onError = {
            mutableRequestShareAddressResponse.value = Fail(it)
        })
    }
}