package com.tokopedia.chatbot.chatbot2.attachinvoice.domain.usecase

import android.content.res.Resources
import android.text.TextUtils
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.chat_common.data.SendableUiModel
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.chatbot2.attachinvoice.domain.model.InvoiceConstants.DEFAULT_LIMIT
import com.tokopedia.chatbot.chatbot2.attachinvoice.domain.model.InvoiceConstants.IS_SHOW_ALL
import com.tokopedia.chatbot.chatbot2.attachinvoice.domain.model.InvoiceConstants.KEYWORD_KEY
import com.tokopedia.chatbot.chatbot2.attachinvoice.domain.model.InvoiceConstants.LIMIT
import com.tokopedia.chatbot.chatbot2.attachinvoice.domain.model.InvoiceConstants.MESSAGE_ID_KEY
import com.tokopedia.chatbot.chatbot2.attachinvoice.domain.model.InvoiceConstants.PAGE_KEY
import com.tokopedia.chatbot.chatbot2.attachinvoice.domain.model.InvoiceConstants.START_TIME
import com.tokopedia.chatbot.chatbot2.data.invoicelist.api.GetInvoiceListPojo
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import rx.Subscriber
import java.util.*
import javax.inject.Inject

/**
 * Created by Hendri on 21/03/18.
 */

class GetInvoiceListUseCase @Inject
constructor(
    val resources: Resources,
    private val graphqlUseCase: GraphqlUseCase
) {

    fun execute(requestParams: Map<String, Any>, subscriber: Subscriber<GraphqlResponse>) {
        val query = GraphqlHelper.loadRawString(resources, R.raw.query_get_invoice_list)
        val graphqlRequest = GraphqlRequest(
            query,
            GetInvoiceListPojo::class.java,
            requestParams,
            false
        )

        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        graphqlUseCase.execute(subscriber)
    }

    companion object {

        fun createRequestParam(query: String, page: Int, messageId: Int): HashMap<String, Any> {
            val param = RequestParams.create()
            if (!TextUtils.isEmpty(query)) param.putString(KEYWORD_KEY, query)
            param.putString(MESSAGE_ID_KEY, messageId.toString())
            param.putBoolean(IS_SHOW_ALL, false)
            param.putString(START_TIME, SendableUiModel.generateStartTime())
            param.putInt(PAGE_KEY, page)
            param.putInt(LIMIT, DEFAULT_LIMIT)
            return param.parameters
        }
    }

    fun unsubscribe() {
        graphqlUseCase.unsubscribe()
    }
}
