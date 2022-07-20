package com.tokopedia.product.manage.feature.suspend.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.product.manage.feature.suspend.domain.usecase.SuspendReasonUseCase
import com.tokopedia.product.manage.feature.suspend.view.uimodel.SuspendReasonUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class SuspendReasonViewModel @Inject constructor(
    private val suspendReasonUseCase: SuspendReasonUseCase,
    private val dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    val suspendReasonUiModelLiveData: LiveData<Result<SuspendReasonUiModel>>
        get() = _suspendReasonUiModelLiveData
    private val _suspendReasonUiModelLiveData = MutableLiveData<Result<SuspendReasonUiModel>>()

    fun getSuspendReason(productId: String) {
        launchCatchError(dispatcher.io, block = {
            val data = suspendReasonUseCase.execute(productId)
            _suspendReasonUiModelLiveData.postValue(Success(data))
        }, onError = {
            _suspendReasonUiModelLiveData.postValue(Fail(it))
        })
    }

}