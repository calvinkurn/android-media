package com.tokopedia.buyerorder.recharge_download.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.buyerorder.common.BuyerDispatcherProvider
import com.tokopedia.buyerorder.recharge_download.data.OrderDetailRechargeDownloadWebviewEntity
import com.tokopedia.buyerorder.recharge_download.domain.OrderDetailRechargeGetInvoiceUseCase
import com.tokopedia.usecase.coroutines.Result
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author by furqan on 21/01/2021
 */
class OrderDetailRechargeDownloadWebviewViewModel @Inject constructor(
        private val useCase: OrderDetailRechargeGetInvoiceUseCase,
        dispatcherProvider: BuyerDispatcherProvider)
    : BaseViewModel(dispatcherProvider.ui()) {

    private val mutableInvoiceData = MutableLiveData<Result<OrderDetailRechargeDownloadWebviewEntity>>()
    val invoiceData: LiveData<Result<OrderDetailRechargeDownloadWebviewEntity>>
        get() = mutableInvoiceData

    fun fetchInvoiceData(orderId: String) {
        launch {
            val data = useCase.executeSuspend(orderId.toInt())
            mutableInvoiceData.postValue(data)
        }
    }

}