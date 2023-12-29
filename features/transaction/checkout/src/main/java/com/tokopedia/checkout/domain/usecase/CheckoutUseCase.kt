package com.tokopedia.checkout.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.checkout.data.model.request.checkout.CheckoutRequest
import com.tokopedia.checkout.data.model.response.checkout.CheckoutGqlResponse
import com.tokopedia.checkout.domain.mapper.CheckoutMapper
import com.tokopedia.checkout.domain.model.checkout.CheckoutData
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import javax.inject.Inject

class CheckoutUseCase @Inject constructor(
    @ApplicationContext private val graphqlRepository: GraphqlRepository,
    private val checkoutMapper: CheckoutMapper,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<CheckoutRequest, CheckoutData>(dispatchers.io) {

    companion object {
        const val CHECKOUT_PARAM_CARTS = "carts"
    }

    @GqlQuery("CheckoutQuery", CHECKOUT_QUERY)
    override fun graphqlQuery(): String {
        return CHECKOUT_QUERY
    }

    override suspend fun execute(params: CheckoutRequest): CheckoutData {
        val request = GraphqlRequest(
            CheckoutQuery(),
            CheckoutGqlResponse::class.java,
            mapOf(CHECKOUT_PARAM_CARTS to params)
        )
        val checkoutGqlResponse =
            graphqlRepository.response(listOf(request)).getSuccessData<CheckoutGqlResponse>()
        return checkoutMapper.convertCheckoutData(checkoutGqlResponse.checkout)
    }
}
