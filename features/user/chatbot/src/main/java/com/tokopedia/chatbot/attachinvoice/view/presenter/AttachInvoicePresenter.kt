package com.tokopedia.chatbot.attachinvoice.view.presenter


import android.content.Context
import com.tokopedia.chatbot.attachinvoice.domain.usecase.GetInvoiceListUseCase
import com.tokopedia.chatbot.attachinvoice.view.AttachInvoiceContract
import com.tokopedia.chatbot.attachinvoice.view.subscriber.AttachInvoicesLoadInvoiceDataSubscriber
import javax.inject.Inject

/**
 * Created by Hendri on 22/03/18.
 */

class AttachInvoicePresenter @Inject constructor(private val useCase: GetInvoiceListUseCase)
    : AttachInvoiceContract.Presenter {
    internal var activity: AttachInvoiceContract.Activity? = null
    internal var view: AttachInvoiceContract.View? = null

    override fun loadInvoiceData(query: String, userId: String, page: Int, messageId: Int, context: Context) {
        useCase.execute(GetInvoiceListUseCase.createRequestParam(query, page, messageId),
                AttachInvoicesLoadInvoiceDataSubscriber(view!!))
    }

    override fun attachView(view: AttachInvoiceContract.View) {
        this.view = view
    }

    override fun attachActivityContract(activityContract: AttachInvoiceContract.Activity) {
        this.activity = activityContract
    }

    override fun detachView() {
        useCase.unsubscribe()
        if (view != null) view = null
        if (activity != null) activity = null
    }
}
