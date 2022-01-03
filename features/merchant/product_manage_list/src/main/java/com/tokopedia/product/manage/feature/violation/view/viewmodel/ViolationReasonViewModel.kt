package com.tokopedia.product.manage.feature.violation.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.product.manage.feature.violation.domain.usecase.ViolationReasonUseCase
import com.tokopedia.product.manage.feature.violation.view.uimodel.ViolationReasonUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ViolationReasonViewModel @Inject constructor(
    private val violationReasonUseCase: ViolationReasonUseCase,
    private val dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    val violationReasonUiModelLiveData: LiveData<Result<ViolationReasonUiModel>>
        get() = _violationReasonUiModelLiveData
    private val _violationReasonUiModelLiveData = MutableLiveData<Result<ViolationReasonUiModel>>()

    fun getViolationReason(productId: String) {
        launchCatchError(block = {
            _violationReasonUiModelLiveData.value = Success(
                withContext(dispatcher.io) {
                    violationReasonUseCase.execute(productId.toLongOrZero())
                }
            )
        }, onError = {
            _violationReasonUiModelLiveData.value = Fail(it)
        })
    }

}