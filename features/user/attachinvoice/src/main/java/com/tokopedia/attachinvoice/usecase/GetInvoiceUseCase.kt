package com.tokopedia.attachinvoice.usecase

import com.tokopedia.attachinvoice.data.GetInvoiceResponse
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import javax.inject.Inject

class GetInvoiceUseCase @Inject constructor(
        private val gqlUseCase: GraphqlUseCase<GetInvoiceResponse>
) {

    private val paramMsgId = "msgId"
    private val paramPage = "page"

    fun getInvoices(
            onSuccess: (GetInvoiceResponse) -> Unit,
            onError: (Throwable) -> Unit,
            msgId: Int,
            page: Int
    ) {
        val params = generateParams(msgId, page)
        gqlUseCase.apply {
            setTypeClass(GetInvoiceResponse::class.java)
            setRequestParams(params)
            setGraphqlQuery(query)
            execute({ result ->
                onSuccess(result)
            }, { error ->
                onError(error)
            })
        }
    }

    private fun generateParams(msgId: Int, page: Int): Map<String, Any> {
        return mapOf(
                paramMsgId to msgId,
                paramPage to page
        )
    }

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
}