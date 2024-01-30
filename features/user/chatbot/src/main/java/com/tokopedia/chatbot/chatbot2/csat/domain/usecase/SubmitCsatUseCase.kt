package com.tokopedia.chatbot.chatbot2.csat.domain.usecase

import com.tokopedia.chatbot.chatbot2.csat.data.request.SubmitCsatRequest
import com.tokopedia.chatbot.chatbot2.csat.data.response.SubmitCsatResponse
import com.tokopedia.chatbot.chatbot2.csat.domain.usecase.SubmitCsatUseCase.Companion.SUBMIT_CSAT_QUERY
import com.tokopedia.chatbot.chatbot2.csat.domain.usecase.SubmitCsatUseCase.Companion.SUBMIT_CSAT_QUERY_NAME
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import javax.inject.Inject

@GqlQuery(SUBMIT_CSAT_QUERY_NAME, SUBMIT_CSAT_QUERY)
class SubmitCsatUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<SubmitCsatResponse>(graphqlRepository) {

    init {
        setGraphqlQuery(SubmitCsatQuery())
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(SubmitCsatResponse::class.java)
    }

    fun setRequestParams(submitCsatRequest: SubmitCsatRequest) {
        setRequestParams(mapOf("input" to submitCsatRequest))
    }

    companion object {
        const val SUBMIT_CSAT_QUERY_NAME = "SubmitCsatQuery"
        const val SUBMIT_CSAT_QUERY = """
            mutation chipSubmitChatCSAT(${'$'}input: SubmitChatCSATRequest!) {
                chipSubmitChatCSAT(input: ${'$'}input) {
                    status
                    serverProcessTime
                    data {
                      isSuccess
                      toasterMessage
                    }
                    messageError
                }
            }
        """
    }
}
