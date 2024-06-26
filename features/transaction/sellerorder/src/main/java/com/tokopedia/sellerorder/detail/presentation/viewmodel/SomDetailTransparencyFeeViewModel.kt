package com.tokopedia.sellerorder.detail.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.sellerorder.detail.domain.usecase.SomGetFeeTransparencyUseCase
import com.tokopedia.sellerorder.detail.presentation.model.transparency_fee.TransparencyFeeUiModelWrapper
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class SomDetailTransparencyFeeViewModel @Inject constructor(
    private val dispatcher: CoroutineDispatchers,
    private val getSomTransparencyFeeTransparencyUseCase: SomGetFeeTransparencyUseCase
) : BaseViewModel(dispatcher.main) {

    private val _transparencyFee = MutableLiveData<Result<TransparencyFeeUiModelWrapper>>()
    val transparencyFee: LiveData<Result<TransparencyFeeUiModelWrapper>>
        get() = _transparencyFee


    fun fetchTransparencyFee(orderId: String) {
        launchCatchError(context = dispatcher.io, block = {
            val result = getSomTransparencyFeeTransparencyUseCase.execute(orderId)
            _transparencyFee.postValue(Success(result))
        }, onError = {
            _transparencyFee.postValue(Fail(it))
        })
    }
}
