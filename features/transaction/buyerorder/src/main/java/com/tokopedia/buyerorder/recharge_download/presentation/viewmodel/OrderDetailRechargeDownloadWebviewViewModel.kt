package com.tokopedia.buyerorder.recharge_download.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.buyerorder.common.BuyerDispatcherProvider
import com.tokopedia.buyerorder.recharge_download.data.OrderDetailRechargeDownloadWebviewEntity
import com.tokopedia.buyerorder.recharge_download.domain.OrderDetailRechargeGetInvoiceUseCase
import com.tokopedia.buyerorder.recharge_download.presentation.analytics.OrderDetailRechargeDownloadWebviewAnalytics
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author by furqan on 21/01/2021
 */
class OrderDetailRechargeDownloadWebviewViewModel @Inject constructor(
        private val useCase: OrderDetailRechargeGetInvoiceUseCase,
        private val userSession: UserSessionInterface,
        private val invoiceAnalytics: OrderDetailRechargeDownloadWebviewAnalytics,
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

    fun sendOpenScreen(categoryName: String, productName: String) {
        invoiceAnalytics.rechargeDownloadOpenScreen(
                userSession.isLoggedIn,
                categoryName,
                productName,
                userSession.userId
        )
    }

    fun sendDownload(categoryName: String, productName: String) {
        invoiceAnalytics.rechargeInvoiceClickDownload(
                categoryName,
                productName,
                userSession.userId
        )
    }

}