package com.tokopedia.interestpick.domain.usecase

import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.interestpick.data.pojo.GetInterestData
import com.tokopedia.interestpick.data.raw.GQL_QUERY_GET_INTEREST
import rx.Subscriber
import javax.inject.Inject

/**
 * @author by milhamj on 07/09/18.
 */
@Deprecated("use GetInterestPickUseCase.kt instead", ReplaceWith("GetInterestPickUseCase"))
class GetInterestUseCase @Inject constructor(val graphqlUseCase: GraphqlUseCase) {
    fun execute(subscriber: Subscriber<GraphqlResponse>) {
        val query = GQL_QUERY_GET_INTEREST
        val graphqlRequest = GraphqlRequest(query, GetInterestData::class.java)

        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        graphqlUseCase.execute(subscriber)
    }

    fun unsubscribe() = graphqlUseCase.unsubscribe()
}