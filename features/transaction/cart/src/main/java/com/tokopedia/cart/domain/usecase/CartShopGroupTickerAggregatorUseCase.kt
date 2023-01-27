package com.tokopedia.cart.domain.usecase

import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.cart.data.model.response.cartshoptickeraggregator.CartShopGroupTickerAggregatorResponse
import com.tokopedia.cart.data.model.request.CartShopGroupTickerAggregatorParam
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import javax.inject.Inject

@GqlQuery("CartShopGroupTickerAggregatorQuery", CartShopGroupTickerAggregatorUseCase.QUERY)
class CartShopGroupTickerAggregatorUseCase @Inject constructor(
    @ApplicationContext private val graphqlRepository: GraphqlRepository,
    dispatchers: CoroutineDispatchers,
) : CoroutineUseCase<CartShopGroupTickerAggregatorParam, CartShopGroupTickerAggregatorResponse>(
    dispatchers.io
) {

    override fun graphqlQuery(): String {
        return QUERY
    }

    // buat class baru untuk param, include RatesParam + enableBo + enableBundle
    override suspend fun execute(params: CartShopGroupTickerAggregatorParam): CartShopGroupTickerAggregatorResponse {
//        TODO: Remove this after staging BE available
        return Gson().fromJson(DUMMY_SUCCESS, CartShopGroupTickerAggregatorResponse::class.java)
//        return Gson().fromJson(DUMMY_FAILED, CartShopGroupTickerAggregatorResponse::class.java)
        return graphqlRepository.request(CartShopGroupTickerAggregatorQuery(), params.toMap())
    }

    companion object {
        const val QUERY = """
            query cartShopGroupTickerAggregator(${'$'}params: CartShopGroupTickerAggregatorParams!) {
                cart_shop_group_ticker_aggregator(cartShopGroupTickerAggregatorParams: ${'$'}params) {
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
                        }
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
                  "icon": {
                    "left_icon": "https://assets.tokopedia.net/asts/cartapp/icons/courier_fast.svg",
                    "left_icon_dark": "https://assets.tokopedia.net/asts/cartapp/icons/courier_fast.svg",
                    "right_icon": "https://images.tokopedia.net/img/cartapp/icons/chevron_right_grey.png",
                    "right_icon_dark": "https://images.tokopedia.net/img/cartapp/icons/chevron_right_grey.png"
                  },
                  "applink": "tokopedia://now",
                  "action": "open_bottomsheet_bundling"
                },
                "bundle_bottomsheet": {
                    "title": "Dapatkan bebas ongkir",
                    "description": "Dengan beli Paket Bundling atau beli produk lain, kamu bisa dapat Bebas Ongkir.",
                    "bottom_ticker": "Tidak mau beli paketan? Cek produk lain di <a href=\"tokopedia://shop/480552\">toko ini</a> dan <a href=\"tokopedia://now\">toko 2</a>"
                }
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
                "bundle_bottomsheet": {}
              }
            }
        """
    }
}
