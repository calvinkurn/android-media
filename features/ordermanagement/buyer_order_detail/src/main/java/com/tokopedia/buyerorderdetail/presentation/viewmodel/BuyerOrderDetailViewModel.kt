package com.tokopedia.buyerorderdetail.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailParams
import com.tokopedia.buyerorderdetail.domain.usecases.GetBuyerOrderDetailUseCase
import com.tokopedia.buyerorderdetail.presentation.model.ActionButtonsUiModel
import com.tokopedia.buyerorderdetail.presentation.model.BuyerOrderDetailUiModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class BuyerOrderDetailViewModel @Inject constructor(
        coroutineDispatchers: CoroutineDispatchers,
        private val getBuyerOrderDetailUseCase: GetBuyerOrderDetailUseCase
) : BaseViewModel(coroutineDispatchers.io) {

    private val _buyerOrderDetailResult: MutableLiveData<Result<BuyerOrderDetailUiModel>> = MutableLiveData()
    val buyerOrderDetailResult: LiveData<Result<BuyerOrderDetailUiModel>>
        get() = _buyerOrderDetailResult

    fun getBuyerOrderDetail(orderId: String, paymentId: String, cart: String = "") {
        launchCatchError(block = {
            val param = GetBuyerOrderDetailParams(cart, orderId, paymentId)
            _buyerOrderDetailResult.postValue(Success(getBuyerOrderDetailUseCase.execute(param)))
        }, onError = {
            _buyerOrderDetailResult.postValue(Fail(it))
        })
    }

    fun getSecondaryActionButtons(): List<ActionButtonsUiModel.ActionButton> {
        val buyerOrderDetailResult = buyerOrderDetailResult.value
        return if (buyerOrderDetailResult is Success) {
            buyerOrderDetailResult.data.actionButtonsUiModel.secondaryActionButtons
        } else emptyList()
    }
}