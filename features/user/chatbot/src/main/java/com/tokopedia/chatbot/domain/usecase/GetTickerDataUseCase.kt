package com.tokopedia.chatbot.domain.usecase

import com.tokopedia.chatbot.data.TickerData.TickerDataResponse
import com.tokopedia.chatbot.domain.gqlqueries.GetTickerDataQuery
import com.tokopedia.chatbot.domain.gqlqueries.queries.GET_TICKER_DATA
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import javax.inject.Inject

@GqlQuery("chipGetActiveTickerV4", GET_TICKER_DATA)
class GetTickerDataUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<TickerDataResponse>(graphqlRepository) {

    fun getTickerData(
        onSuccess: (TickerDataResponse) -> Unit,
        onError: kotlin.reflect.KFunction2<Throwable, String, Unit>,
        messageId: String
    ) {
        try {
            this.setTypeClass(TickerDataResponse::class.java)
            this.setGraphqlQuery(GetTickerDataQuery())

            this.execute(
                { result ->
                    onSuccess(result)
                }, { error ->
                    onError(error, messageId)
                }
            )
        } catch (throwable: Throwable) {
            onError(throwable, messageId)
        }
    }

}
