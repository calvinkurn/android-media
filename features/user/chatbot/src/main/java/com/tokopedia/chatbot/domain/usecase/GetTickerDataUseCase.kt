package com.tokopedia.chatbot.domain.usecase

import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.data.TickerData.TickerData
import com.tokopedia.chatbot.data.TickerData.TickerDataResponse
import com.tokopedia.chatbot.domain.gqlqueries.GetTickerDataQuery
import com.tokopedia.chatbot.domain.gqlqueries.queries.GET_TICKER_DATA
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import rx.Subscriber
import javax.inject.Inject

@GqlQuery("chipGetActiveTickerV4", GET_TICKER_DATA)
class GetTickerDataUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
) : GraphqlUseCase<TickerDataResponse>(graphqlRepository) {

    fun getTickerData(
        onSuccess: (TickerDataResponse) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        try {
            this.setTypeClass(TickerDataResponse::class.java)
            this.setGraphqlQuery(GetTickerDataQuery())

            this.execute(
                { result ->
                    onSuccess(result)
                }, { error ->
                    onError(error)
                }
            )
        } catch (throwable: Throwable) {
            onError(throwable)
        }
    }

//    fun execute(subscriber: Subscriber<GraphqlResponse>) {
//        val query = GraphqlHelper.loadRawString(resources, R.raw.query_chip_get_active_ticker)
//        val graphqlRequest = GraphqlRequest(query,
//                TickerDataResponse::class.java, false)
//
//        graphqlUseCase.clearRequest()
//        graphqlUseCase.addRequest(graphqlRequest)
//        graphqlUseCase.execute(subscriber)
//    }
//
//
//    fun unsubscribe() {
//        graphqlUseCase.unsubscribe()
//    }

}