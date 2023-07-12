package com.tokopedia.cartrevamp.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.cartrevamp.data.model.request.CartShopGroupTickerAggregatorParam
import com.tokopedia.cartrevamp.data.model.response.cartshoptickeraggregator.CartShopGroupTickerAggregatorGqlResponse
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import javax.inject.Inject

@GqlQuery("CartShopGroupTickerAggregatorQuery", CartShopGroupTickerAggregatorUseCase.QUERY)
class CartShopGroupTickerAggregatorUseCase @Inject constructor(
    @ApplicationContext private val graphqlRepository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<CartShopGroupTickerAggregatorParam, CartShopGroupTickerAggregatorGqlResponse>(
    dispatchers.io
) {

    override fun graphqlQuery(): String {
        return QUERY
    }

    override suspend fun execute(params: CartShopGroupTickerAggregatorParam): CartShopGroupTickerAggregatorGqlResponse {
        return graphqlRepository.request(
            CartShopGroupTickerAggregatorQuery(),
            mapOf("params" to params.toMap())
        )
    }

    companion object {
        const val QUERY = """
            query cartShopGroupTickerAggregator(${'$'}params: CartShopGroupTickerAggregatorParams) {
                cart_shop_group_ticker_aggregator(params: ${'$'}params) {
                    error_message
                    status
                    data {
                        min_transaction
                        ticker {
                            text
                            icon {
                                left_icon
                                left_icon_dark
                                right_icon
                                right_icon_dark
                            }
                            applink
                            action
                        }
                        bundle_bottomsheet {
                            title
                            description
                            bottom_ticker
                            bundle_ids
                        }
                    }
                }
            }
        """
    }
}
