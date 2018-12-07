package com.tokopedia.chatbot.attachinvoice.domain.usecase

import android.content.Context
import android.text.TextUtils

import com.tokopedia.chatbot.attachinvoice.data.repository.AttachInvoicesRepository
import com.tokopedia.chatbot.attachinvoice.view.model.InvoiceToInvoiceViewModelMapper
import com.tokopedia.chatbot.attachinvoice.view.model.InvoiceViewModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase

import javax.inject.Inject

import rx.Observable

/**
 * Created by Hendri on 21/03/18.
 */

class AttachInvoicesUseCase @Inject
constructor(internal var repository: AttachInvoicesRepository) : UseCase<List<InvoiceViewModel>>() {

    override fun createObservable(requestParams: RequestParams): Observable<List<InvoiceViewModel>> {
        return repository.getUserInvoices(requestParams.paramsAllValueInString).map(InvoiceToInvoiceViewModelMapper())
    }

    companion object {
        val KEYWORD_KEY = "keyword"
        val USER_ID_KEY = "user_id"
        val PAGE_KEY = "page"
        val MESSAGE_ID_KEY = "message_id"
        const val DEFAULT_LIMIT = 10


        fun createRequestParam(query: String, userId: String, page: Int, messageId: Int, context: Context): RequestParams {
            val param = RequestParams.create()
            if (!TextUtils.isEmpty(query)) param.putString(KEYWORD_KEY, query)
            param.putString(USER_ID_KEY, userId)
            param.putString(PAGE_KEY, page.toString())
            param.putString(MESSAGE_ID_KEY, messageId.toString())
            return param
        }
    }
}
