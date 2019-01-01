package com.tokopedia.chatbot.attachinvoice.view.subscriber


import android.util.Log
import com.tokopedia.chatbot.attachinvoice.domain.usecase.AttachInvoicesUseCase
import com.tokopedia.chatbot.attachinvoice.view.AttachInvoiceContract
import com.tokopedia.chatbot.attachinvoice.view.model.InvoiceViewModel
import com.tokopedia.graphql.data.model.GraphqlResponse

import rx.Subscriber

/**
 * Created by Hendri on 22/03/18.
 */

class AttachInvoicesLoadInvoiceDataSubscriber(private val view: AttachInvoiceContract.View) : Subscriber<GraphqlResponse>() {

    override fun onCompleted() {

    }

    override fun onError(e: Throwable) {
        view.showErrorMessage(e)
    }

    override fun onNext(response: GraphqlResponse) {
        //TODO
        Log.d("NIS", response.toString())
//        view.addInvoicesToList(invoiceViewModels,
//                invoiceViewModels.size >= AttachInvoicesUseCase.DEFAULT_LIMIT)
    }
}
