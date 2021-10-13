package com.tokopedia.buyerorderdetail.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.buyerorderdetail.presentation.model.OrderExtensionRespondInfoUiModel
import com.tokopedia.buyerorderdetail.presentation.model.OrderExtensionRespondUiModel
import com.tokopedia.usecase.coroutines.Result
import javax.inject.Inject

class BuyerOrderDetailExtensionViewModel @Inject constructor(
    dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    private val _orderExtensionRespondInfo: MutableLiveData<Result<OrderExtensionRespondInfoUiModel>> =
        MutableLiveData()
    val orderExtensionRespondInfo: LiveData<Result<OrderExtensionRespondInfoUiModel>>
        get() = _orderExtensionRespondInfo

    private val _orderExtensionRespond: MutableLiveData<Result<OrderExtensionRespondUiModel>> =
        MutableLiveData()
    val orderExtensionRespond: LiveData<Result<OrderExtensionRespondUiModel>>
        get() = _orderExtensionRespond

    fun requestRespondInfo(orderId: String) {

    }

    fun requestRespond(orderId: String) {

    }

}