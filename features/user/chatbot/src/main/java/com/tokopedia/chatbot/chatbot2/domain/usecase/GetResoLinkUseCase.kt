package com.tokopedia.chatbot.chatbot2.domain.usecase

import com.tokopedia.chatbot.chatbot2.data.resolink.ResoLinkResponse
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import javax.inject.Inject

@GqlQuery(
    "GetResoLinkQuery",
    com.tokopedia.chatbot.chatbot2.domain.gqlqueries.queries.GET_RESO_LINK_QUERY
)
class GetResoLinkUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<ResoLinkResponse>(graphqlRepository) {

    // TODO need testing
    fun getResolutionLink(
        onSuccess: (ResoLinkResponse) -> Unit,
        onError: kotlin.reflect.KFunction2<Throwable, String, Unit>,
        invoiceRefNum: String,
        messageId: String
    ) {
        try {
            this.setTypeClass(ResoLinkResponse::class.java)
            this.setRequestParams(getParams(invoiceRefNum))
            this.setGraphqlQuery(com.tokopedia.chatbot.chatbot2.domain.gqlqueries.GetResolutionLinkQuery())

            this.execute({
                onSuccess(it)
            }, {
                onError(it, messageId)
            })
        } catch (throwable: Throwable) {
            onError(throwable, messageId)
        }
    }

    private fun getParams(invoiceRefNumber: String): Map<String, Any?> {
        val map = mapOf<String, Any?>(
            INVOICE_REF_NUMBER to invoiceRefNumber,
            QUERY_REFERENCE_KEY to QUERY_REFERENCE_VALUE
        )
        return mapOf(
            QUERY_INPUT to map
        )
    }

    companion object {
        const val INVOICE_REF_NUMBER = "invoice_ref_num"
        const val QUERY_REFERENCE_KEY = "ref"
        const val QUERY_REFERENCE_VALUE = "chatbot"
        const val QUERY_INPUT = "input"
    }
}
