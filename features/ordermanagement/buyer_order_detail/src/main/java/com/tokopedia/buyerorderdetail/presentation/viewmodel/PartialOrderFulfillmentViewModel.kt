package com.tokopedia.buyerorderdetail.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.buyerorderdetail.domain.usecases.ApprovePartialOrderFulfillmentUseCase
import com.tokopedia.buyerorderdetail.domain.usecases.GetPartialOrderFulfillmentInfoUseCase
import com.tokopedia.buyerorderdetail.domain.usecases.RejectPartialOrderFulfillmentUseCase
import com.tokopedia.buyerorderdetail.presentation.model.ApprovePartialOrderFulfillmentUiModel
import com.tokopedia.buyerorderdetail.presentation.model.PartialOrderFulfillmentWrapperUiModel
import com.tokopedia.buyerorderdetail.presentation.model.RejectPartialOrderFulfillmentUiModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PartialOrderFulfillmentViewModel @Inject constructor(
    private val dispatcher: CoroutineDispatchers,
    private val getPartialOrderFulfillmentInfoUseCase: dagger.Lazy<GetPartialOrderFulfillmentInfoUseCase>,
    private val approvePartialOrderFulfillmentUseCase: dagger.Lazy<ApprovePartialOrderFulfillmentUseCase>,
    private val rejectPartialOrderFulfillmentUseCase: dagger.Lazy<RejectPartialOrderFulfillmentUseCase>
) : BaseViewModel(dispatcher.main) {

    private val _partialOrderFulfillmentRespondInfo: MutableLiveData<Result<PartialOrderFulfillmentWrapperUiModel>> =
        MutableLiveData()
    val partialOrderFulfillmentRespondInfo: LiveData<Result<PartialOrderFulfillmentWrapperUiModel>>
        get() = _partialOrderFulfillmentRespondInfo

    private val _approvePartialOrderFulfillment: MutableLiveData<Result<ApprovePartialOrderFulfillmentUiModel>> =
        MutableLiveData()
    val approvePartialOrderFulfillment: LiveData<Result<ApprovePartialOrderFulfillmentUiModel>>
        get() = _approvePartialOrderFulfillment

    private val _rejectPartialOrderFulfillment: MutableLiveData<Result<RejectPartialOrderFulfillmentUiModel>> =
        MutableLiveData()

    val rejectPartialOrderFulfillment: LiveData<Result<RejectPartialOrderFulfillmentUiModel>>
        get() = _rejectPartialOrderFulfillment

    fun fetchPartialOrderFulfillment(orderId: String) {
        launchCatchError(block = {
            val response = withContext(dispatcher.io) {
                getPartialOrderFulfillmentInfoUseCase.get().execute(orderId)
            }
            _partialOrderFulfillmentRespondInfo.value = Success(response)
        }, onError = {
                _partialOrderFulfillmentRespondInfo.value = Fail(it)
            })
    }

    fun approvePartialOrderFulfillment(orderId: String) {
        launchCatchError(block = {
            val response = withContext(dispatcher.io) {
                approvePartialOrderFulfillmentUseCase.get().execute(orderId)
            }
            _approvePartialOrderFulfillment.value = Success(response)
        }, onError = {
                _approvePartialOrderFulfillment.value = Fail(it)
            })
    }

    fun rejectPartialOrderFulfillment(orderId: String) {
        launchCatchError(block = {
            val response = withContext(dispatcher.io) {
                rejectPartialOrderFulfillmentUseCase.get().execute(orderId)
            }
            _rejectPartialOrderFulfillment.value = Success(response)
        }, onError = {
                _rejectPartialOrderFulfillment.value = Fail(it)
            })
    }
}
