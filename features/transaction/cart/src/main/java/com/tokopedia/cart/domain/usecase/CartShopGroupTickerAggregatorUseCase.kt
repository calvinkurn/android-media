package com.tokopedia.cart.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.cart.data.model.response.cartshoptickeraggregator.CartShopGroupTickerAggregatorResponse
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.logisticcart.shipping.model.RatesParam
import javax.inject.Inject

@GqlQuery("CartShopGroupTickerAggregatorQuery", CartShopGroupTickerAggregatorUseCase.QUERY)
class CartShopGroupTickerAggregatorUseCase @Inject constructor(
    @ApplicationContext private val graphqlRepository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<RatesParam, CartShopGroupTickerAggregatorResponse>(dispatchers.io) {

    var enableBoAffordability: Boolean = false
    var enableBundleCrossSell: Boolean = false

    override fun graphqlQuery(): String {
        return QUERY
    }

    override suspend fun execute(params: RatesParam): CartShopGroupTickerAggregatorResponse {
        val cartAggregatorParams = params.toCartShopGroupTickerAggregatorMap().toMutableMap()
        cartAggregatorParams[PARAM_ENABLE_BO_AFFORDABILITY] = enableBoAffordability
        cartAggregatorParams[PARAM_ENABLE_BUNDLE_CROSS_SELL] = enableBundleCrossSell
        return graphqlRepository.request(CartShopGroupTickerAggregatorQuery(), cartAggregatorParams)
    }

    companion object {
        private const val PARAM_ENABLE_BO_AFFORDABILITY = "enable_bo_affordability"
        private const val PARAM_ENABLE_BUNDLE_CROSS_SELL = "enable_bundle_cross_sell"

        const val QUERY = """
            query cartShopGroupTickerAggregator(${'$'}params: CartShopGroupTickerAggregatorParams!) {
                cart_shop_group_ticker_aggregator(cartShopGroupTickerAggregatorParams: ${'$'}params) {
                    error_message
                    status
                    data {
                        min_transaction
                        ticker {
                            text
                            left_icon
                            right_icon
                        }
                        bundle_bottomsheet {
                            title
                            description
                            bottom_ticker
                        }
                        aggregator_type
                    }
                }
            }
        """
    }
}
