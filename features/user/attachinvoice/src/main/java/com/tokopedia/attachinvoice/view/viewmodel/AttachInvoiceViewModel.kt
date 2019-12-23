package com.tokopedia.attachinvoice.view.viewmodel

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.applink.ApplinkConst
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

    var messageId: String = ""
    var opponentName: String = ""

    private var _invoices = MutableLiveData<Result<List<Invoice>>>()
    val invoices get() = _invoices

    fun loadInvoices(page: Int) {
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

    fun initializeArguments(arguments: Bundle?) {
        if (arguments == null) return
        messageId = arguments.getString(ApplinkConst.AttachInvoice.PARAM_MESSAGE_ID, "")
        opponentName = arguments.getString(ApplinkConst.AttachInvoice.PARAM_OPPONENT_NAME, "")
    }

    fun getInvoicePreviewIntent(invoice: Invoice): Intent {
        return Intent().apply {
            putExtra(ApplinkConst.Chat.INVOICE_ID, invoice.id.toString())
            putExtra(ApplinkConst.Chat.INVOICE_CODE, invoice.code)
            putExtra(ApplinkConst.Chat.INVOICE_TITLE, invoice.productName)
            putExtra(ApplinkConst.Chat.INVOICE_DATE, invoice.timeStamp)
            putExtra(ApplinkConst.Chat.INVOICE_IMAGE_URL, invoice.thumbnailUrl)
            putExtra(ApplinkConst.Chat.INVOICE_URL, invoice.url)
            putExtra(ApplinkConst.Chat.INVOICE_STATUS_ID, invoice.statusId.toString())
            putExtra(ApplinkConst.Chat.INVOICE_STATUS, invoice.status)
            putExtra(ApplinkConst.Chat.INVOICE_TOTAL_AMOUNT, invoice.productPrice)
        }
    }

}