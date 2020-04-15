package com.tokopedia.attachinvoice.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.attachinvoice.data.GetInvoiceResponse
import com.tokopedia.attachinvoice.data.Invoice
import com.tokopedia.attachinvoice.usecase.GetInvoiceUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class AttachInvoiceViewModel @Inject constructor(
        private val getInvoiceUseCase: GetInvoiceUseCase
) : ViewModel() {

    private var _invoices = MutableLiveData<Result<List<Invoice>>>()
    val invoices: LiveData<Result<List<Invoice>>> get() = _invoices

    fun loadInvoices(page: Int, messageId: String) {
        if (messageId.isEmpty()) return
        getInvoiceUseCase.getInvoices(
                ::onSuccessGetInvoice,
                ::onErrorGetInvoice,
                messageId.toInt(),
                page
        )
    }

    private fun onSuccessGetInvoice(response: GetInvoiceResponse) {
        _invoices.value = Success(response.invoices)
    }

    private fun onErrorGetInvoice(throwable: Throwable) {
        _invoices.value = Fail(throwable)
    }

}