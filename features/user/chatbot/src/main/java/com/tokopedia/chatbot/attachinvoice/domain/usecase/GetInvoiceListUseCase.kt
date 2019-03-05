package com.tokopedia.chatbot.attachinvoice.domain.usecase

import android.content.res.Resources
import android.text.TextUtils
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.chat_common.data.SendableViewModel

import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.attachinvoice.data.model.GetInvoicesResponsePojo
import com.tokopedia.chatbot.domain.pojo.invoicelist.api.GetInvoiceListPojo
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse

import javax.inject.Inject

import rx.Subscriber
import java.util.HashMap

/**
 * Created by Hendri on 21/03/18.
 */

class GetInvoiceListUseCase @Inject
constructor(val resources: Resources,
            private val graphqlUseCase: GraphqlUseCase) {

    fun execute(requestParams: Map<String, Any>, subscriber: Subscriber<GraphqlResponse>) {
        val query = GraphqlHelper.loadRawString(resources, R.raw.query_get_invoice_list)
        val graphqlRequest = GraphqlRequest(query,
                GetInvoiceListPojo::class.java, requestParams, false)

        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        graphqlUseCase.execute(subscriber)
    }

    companion object {
        val KEYWORD_KEY = "keyword"
        val PAGE_KEY = "page"
        val LIMIT = "limit"
        val IS_SHOW_ALL = "showAll"
        val START_TIME = "startTime"
        val MESSAGE_ID_KEY = "msgId"
        const val DEFAULT_LIMIT = 10

        fun createRequestParam(query: String, page: Int, messageId: Int): HashMap<String, Any> {
            val param = RequestParams.create()
            if (!TextUtils.isEmpty(query)) param.putString(KEYWORD_KEY, query)
            param.putString(MESSAGE_ID_KEY, messageId.toString())
            param.putBoolean(IS_SHOW_ALL, false)
            param.putString(START_TIME, SendableViewModel.generateStartTime())
            param.putInt(PAGE_KEY, page)
            param.putInt(LIMIT, DEFAULT_LIMIT)
            return param.parameters
        }
    }

    fun unsubscribe() {
        graphqlUseCase.unsubscribe()
    }
}
