package com.tokopedia.chatbot.attachinvoice.view.subscriber


import com.tokopedia.chatbot.attachinvoice.domain.usecase.AttachInvoicesUseCase
import com.tokopedia.chatbot.attachinvoice.view.AttachInvoiceContract
import com.tokopedia.chatbot.attachinvoice.view.model.InvoiceViewModel

import rx.Subscriber

/**
 * Created by Hendri on 22/03/18.
 */

class AttachInvoicesLoadInvoiceDataSubscriber(private val view: AttachInvoiceContract.View) : Subscriber<List<InvoiceViewModel>>() {

    override fun onCompleted() {

    }

    override fun onError(e: Throwable) {
        view.showErrorMessage(e)
    }

    override fun onNext(invoiceViewModels: List<InvoiceViewModel>) {
        view.addInvoicesToList(invoiceViewModels,
                invoiceViewModels.size >= AttachInvoicesUseCase.DEFAULT_LIMIT)
    }
}
