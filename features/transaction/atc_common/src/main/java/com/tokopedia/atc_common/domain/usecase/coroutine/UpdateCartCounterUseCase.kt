package com.tokopedia.atc_common.domain.usecase.coroutine

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.domain.model.response.updatecartcounter.UpdateCartCounterGqlResponse
import com.tokopedia.atc_common.domain.usecase.query.UPDATE_CART_COUNTER_QUERY
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import javax.inject.Inject

class UpdateCartCounterUseCase @Inject constructor(
    @ApplicationContext private val graphqlRepository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<Unit, Int>(dispatcher.io) {

    companion object {
        const val PARAM_PRODUCT_ID = "productID"
        const val QUERY_UPDATE_CART_COUNTER = "UpdateCartCounterQuery"
    }

    override fun graphqlQuery(): String = ""

    @GqlQuery(QUERY_UPDATE_CART_COUNTER, UPDATE_CART_COUNTER_QUERY)
    override suspend fun execute(params: Unit): Int {
        val request = GraphqlRequest(
            UpdateCartCounterQuery(),
            UpdateCartCounterGqlResponse::class.java
        )
        val response = graphqlRepository.response(listOf(request))
            .getSuccessData<UpdateCartCounterGqlResponse>()
        return response.updateCartCounter.count
    }
}
