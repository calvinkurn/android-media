package com.tokopedia.chatbot.chatbot2.attachinvoice.domain.usecase

import com.tokopedia.chat_common.data.SendableUiModel
import com.tokopedia.chatbot.chatbot2.attachinvoice.domain.model.InvoiceConstants.DEFAULT_LIMIT
import com.tokopedia.chatbot.chatbot2.attachinvoice.domain.model.InvoiceConstants.FILTERED_EVENT
import com.tokopedia.chatbot.chatbot2.attachinvoice.domain.model.InvoiceConstants.IS_SHOW_ALL
import com.tokopedia.chatbot.chatbot2.attachinvoice.domain.model.InvoiceConstants.LIMIT
import com.tokopedia.chatbot.chatbot2.attachinvoice.domain.model.InvoiceConstants.MESSAGE_ID_KEY
import com.tokopedia.chatbot.chatbot2.attachinvoice.domain.model.InvoiceConstants.PAGE_KEY
import com.tokopedia.chatbot.chatbot2.attachinvoice.domain.model.InvoiceConstants.START_TIME
import com.tokopedia.chatbot.chatbot2.data.invoicelist.api.GetInvoiceListPojo
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import javax.inject.Inject

const val GET_FILTERED_INVOICE_QUERY = """query get_invoice_list(${'$'}msgId: String!, ${'$'}showAll: Boolean!, 
    ${'$'}startTime: String!, ${'$'}limit: Int!, ${'$'}page: Int!, ${'$'}filterEvent: String!) {
  getInvoiceList(msgID: ${'$'}msgId, showAll: ${'$'}showAll, startTime: ${'$'}startTime, limit: ${'$'}limit, 
  page: ${'$'}page, filterEvent: ${'$'}filterEvent) {
    TypeID
    Type
    IsError
    Attributes {
      ID
      PaymentID
      Code
      Color
      Title
      Description
      CreateTime
      CreateTimeSort
      StatusID
      Status
      StatusTime
      TotalAmount
      ImageURL
      InvoiceURL
      PaymentMethod
      ResolutionID
      FailedTime
    }
    Status {
      Code
      ErrorDetails
    }
  }
}
"""

@GqlQuery("GetFilteredInvoiceQuery", GET_FILTERED_INVOICE_QUERY)
class GetFilteredInvoiceListUseCase @Inject constructor(graphqlRepository: GraphqlRepository) :
    GraphqlUseCase<GetInvoiceListPojo>(graphqlRepository) {

    init {
        setTypeClass(GetInvoiceListPojo::class.java)
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.CLOUD_THEN_CACHE).build())
        setGraphqlQuery(GET_FILTERED_INVOICE_QUERY)
    }

    fun setParams(filteredEvent: String, page: Int, messageId: String) {
        val queryMap = mutableMapOf(
            MESSAGE_ID_KEY to messageId,
            START_TIME to SendableUiModel.generateStartTime(),
            FILTERED_EVENT to filteredEvent,
            PAGE_KEY to page,
            LIMIT to DEFAULT_LIMIT,
            IS_SHOW_ALL to false
        )
        setRequestParams(queryMap)
    }
}
