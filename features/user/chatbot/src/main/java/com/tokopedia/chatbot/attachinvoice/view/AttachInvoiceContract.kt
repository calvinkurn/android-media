package com.tokopedia.chatbot.attachinvoice.view

import android.content.Context

import com.tokopedia.chatbot.attachinvoice.view.model.InvoiceViewModel

/**
 * Created by Hendri on 22/03/18.
 */

interface AttachInvoiceContract {
    interface View {
        fun addInvoicesToList(invoices: List<InvoiceViewModel>, hasNextPage: Boolean)

        fun hideAllLoadingIndicator()

        fun showErrorMessage(throwable: Throwable)
    }

    interface Activity {
        val userId: String

        val messageId: Int
    }

    interface Presenter {
        fun loadInvoiceData(query: String, userId: String, page: Int, messageId: Int, context: Context)
        fun attachView(view: AttachInvoiceContract.View)
        fun attachActivityContract(activityContract: AttachInvoiceContract.Activity)
        fun detachView()
    }
}
