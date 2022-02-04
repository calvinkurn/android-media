package com.tokopedia.attachinvoice.usecase

import com.tokopedia.attachinvoice.data.GetInvoiceResponse
import com.tokopedia.attachinvoice.data.ParamInvoice
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class AttachInvoiceUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatcher
): CoroutineUseCase<ParamInvoice, GetInvoiceResponse>(dispatcher) {

    private val paramMsgId = "msgId"
    private val paramPage = "page"

    private val query = """
        query chatListInvoice($$paramMsgId: Int!, $$paramPage:Int!) {
          chatListInvoice(msgId:$$paramMsgId, page:$$paramPage) {
            invoiceList {
              typeId
              type
              attributes{
                Id
                Code
                Title
                Description
                CreateTime
                ImageURL
                HrefURL
                StatusId
                Status
                TotalAmount
              }
            }
          }
        }
    """.trimIndent()

    override fun graphqlQuery(): String {
        return query
    }

    override suspend fun execute(params: ParamInvoice): GetInvoiceResponse {
        return repository.request(graphqlQuery(), params)
    }
}