package com.tokopedia.chatbot.chatbot2.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import javax.inject.Inject

@GqlQuery(
    "chipGetActiveTickerV4",
    com.tokopedia.chatbot.chatbot2.domain.gqlqueries.queries.GET_TICKER_DATA
)
class GetTickerDataUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<com.tokopedia.chatbot.chatbot2.data.TickerData.TickerDataResponse>(graphqlRepository) {

    fun getTickerData(
        onSuccess: (com.tokopedia.chatbot.chatbot2.data.TickerData.TickerDataResponse) -> Unit,
        onError: kotlin.reflect.KFunction2<Throwable, String, Unit>,
        messageId: String
    ) {
        try {
            this.setTypeClass(com.tokopedia.chatbot.chatbot2.data.TickerData.TickerDataResponse::class.java)
            this.setGraphqlQuery(com.tokopedia.chatbot.chatbot2.domain.gqlqueries.GetTickerDataQuery())

            this.execute(
                { result ->
                    onSuccess(result)
                },
                { error ->
                    onError(error, messageId)
                }
            )
        } catch (throwable: Throwable) {
            onError(throwable, messageId)
        }
    }
}
