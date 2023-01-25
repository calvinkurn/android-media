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
//        TODO: Remove this after staging BE available
//        return Gson().fromJson(DUMMY_SUCCESS, CartShopGroupTickerAggregatorResponse::class.java)
//        return Gson().fromJson(DUMMY_FAILED, CartShopGroupTickerAggregatorResponse::class.java)
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

        // TODO: Remove this after staging BE available
        const val DUMMY_SUCCESS = """
            {
              "error_message": [],
              "status": "SUCCESS",
              "data": {
                "min_transaction": 50000,
                "ticker": {
                  "text": "+Rp10.900 lagi di toko ini, ongkir Rp10.000 <s>Rp30.000</s>",
                  "left_icon": "https://assets.tokopedia.net/asts/cartapp/icons/courier_fast.svg",
                  "right_icon": "https://images.tokopedia.net/img/cartapp/icons/chevron_right_grey.png"
                },
                "bundle_bottomsheet": {
                    "title": "Dapatkan bebas ongkir",
                    "description": "Dengan beli Paket Bundling atau beli produk lain, kamu bisa dapat Bebas Ongkir.",
                    "bottom_ticker": "Tidak mau beli paketan? Cek produk lain di toko ini"
                },
                "aggregator_type": 3
              }
            }
        """
        const val DUMMY_FAILED = """
            {
              "error_message": ["Internal Server Error"],
              "status": "ERROR",
              "data": {
                "min_transaction": 0,
                "ticker": {},
                "bundle_bottomsheet": {},
                "aggregator_type": 0
              }
            }
        """
    }
}
