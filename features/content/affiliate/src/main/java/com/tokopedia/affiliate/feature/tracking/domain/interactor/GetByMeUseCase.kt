package com.tokopedia.affiliate.feature.tracking.domain.interactor

import com.tokopedia.affiliate.feature.tracking.domain.model.Data
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import rx.Subscriber
import java.util.*
import javax.inject.Inject

class GetByMeUseCase @Inject internal constructor(private val graphqlUseCase: GraphqlUseCase) {

    private val query = """
        query getRealURL(${'$'}name: String!, ${'$'}key: String!)
        {
          bymeGetRealURL(affiliateName: ${'$'}name, urlKey: ${'$'}key) {
            realURL
            error {
              type
              code
            }
          }
        }
    """

    fun createObservable(affiliateName: String, urlKey: String,
                         subscriber: Subscriber<GraphqlResponse?>) {
        val variables: MutableMap<String, Any> = HashMap()
        variables[AFFILIATE_NAME] = affiliateName
        variables[URL_KEY] = urlKey
        val graphqlRequest = GraphqlRequest(
                query,
                Data::class.java,
                variables,
                false)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        graphqlUseCase.execute(subscriber)
    }

    fun unsubscribe() {
        graphqlUseCase.unsubscribe()
    }

    companion object {
        private const val AFFILIATE_NAME = "name"
        private const val URL_KEY = "key"
    }
}