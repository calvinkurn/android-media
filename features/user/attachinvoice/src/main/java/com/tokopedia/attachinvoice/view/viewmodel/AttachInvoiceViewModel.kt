package com.tokopedia.attachinvoice.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.attachinvoice.data.GetInvoiceResponse
import com.tokopedia.attachinvoice.data.Invoice
import com.tokopedia.attachinvoice.usecase.GetInvoiceUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class AttachInvoiceViewModel @Inject constructor(
        private val getInvoiceUseCase: GetInvoiceUseCase,
        private val dispatcher: CoroutineDispatcher
) : BaseViewModel(dispatcher) {

    companion object {
        private val paramMsgId = "msgId"
        private val paramPage = "page"
    }

    private var _invoices = MutableLiveData<Result<List<Invoice>>>()
    val invoices: LiveData<Result<List<Invoice>>> get() = _invoices

    fun loadInvoices(page: Int, messageId: String) {
        if (messageId.isEmpty()) return
        launchCatchError(block = {
            val param = generateParams(msgId = messageId.toInt(), page = page)
            val result = getInvoiceUseCase(param)
            onSuccessGetInvoice(result)
        }, onError = {
            onErrorGetInvoice(it)
        })
    }

    private fun generateParams(msgId: Int, page: Int): Map<String, Any> {
        return mapOf(
            paramMsgId to msgId,
            paramPage to page
        )
    }

    private fun onSuccessGetInvoice(response: GetInvoiceResponse) {
        _invoices.value = Success(response.invoices)
    }

    private fun onErrorGetInvoice(throwable: Throwable) {
        _invoices.value = Fail(throwable)
    }
}