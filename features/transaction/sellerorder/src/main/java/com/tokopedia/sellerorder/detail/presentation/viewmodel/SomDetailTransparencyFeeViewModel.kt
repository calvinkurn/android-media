package com.tokopedia.sellerorder.detail.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.sellerorder.detail.presentation.model.BaseTransparencyFee
import com.tokopedia.sellerorder.detail.presentation.model.TransparencyFeeUiModelWrapper
import com.tokopedia.usecase.coroutines.Result
import javax.inject.Inject

class SomDetailTransparencyFeeViewModel @Inject constructor(
    dispatcher: CoroutineDispatchers,
) : BaseViewModel(dispatcher.main) {

    private val _transparencyFee = MutableLiveData<Result<TransparencyFeeUiModelWrapper>>()
    val transparencyFee: LiveData<Result<TransparencyFeeUiModelWrapper>>
        get() = _transparencyFee


    fun fetchTransparencyFee(orderId: String) {
        launchCatchError(block = {

        }, onError = {

        })
    }
}
