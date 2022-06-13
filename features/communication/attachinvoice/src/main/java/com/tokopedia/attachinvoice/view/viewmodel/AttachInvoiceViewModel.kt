package com.tokopedia.attachinvoice.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.attachinvoice.data.GetInvoiceResponse
import com.tokopedia.attachinvoice.data.Invoice
import com.tokopedia.attachinvoice.data.ParamInvoice
import com.tokopedia.attachinvoice.usecase.AttachInvoiceUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class AttachInvoiceViewModel @Inject constructor(
    private val attachInvoiceUseCase: AttachInvoiceUseCase,
    dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    private var _invoices = MutableLiveData<Result<List<Invoice>>>()
    val invoices: LiveData<Result<List<Invoice>>> get() = _invoices

    fun loadInvoices(page: Int, messageId: String) {
        if (messageId.isEmpty()) return
        launchCatchError(block = {
            val param = generateParams(msgId = messageId.toInt(), page = page)
            val result = attachInvoiceUseCase(param)
            onSuccessGetInvoice(result)
        }, onError = {
            onErrorGetInvoice(it)
        })
    }

    private fun generateParams(msgId: Int, page: Int): ParamInvoice {
        return ParamInvoice(msgId, page)
    }

    private fun onSuccessGetInvoice(response: GetInvoiceResponse) {
        _invoices.value = Success(response.invoices)
    }

    private fun onErrorGetInvoice(throwable: Throwable) {
        _invoices.value = Fail(throwable)
    }
}