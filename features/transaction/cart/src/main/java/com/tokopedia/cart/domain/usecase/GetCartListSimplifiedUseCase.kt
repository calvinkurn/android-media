package com.tokopedia.cart.domain.usecase

import com.tokopedia.cart.data.model.response.shopgroupsimplified.ShopGroupSimplifiedGqlResponse
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.purchase_platform.common.data.api.exception.CartResponseErrorException
import com.tokopedia.purchase_platform.common.domain.schedulers.ExecutorSchedulers
import com.tokopedia.cart.domain.mapper.CartSimplifiedMapper
import com.tokopedia.cart.domain.model.cartlist.CartListData
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by Irfan Khoirul on 2019-10-18.
 */

class GetCartListSimplifiedUseCase @Inject constructor(@Named("shopGroupSimplifiedQuery") private val queryString: String,
                                                       private val cartSimplifiedMapper: CartSimplifiedMapper,
                                                       private val schedulers: ExecutorSchedulers) : UseCase<CartListData>() {

    companion object {
        const val PARAM_SELECTED_CART_ID = "PARAM_SELECTED_CART_ID"

        const val PARAM_KEY_LANG = "lang"
        const val PARAM_VALUE_ID = "id"
        const val PARAM_KEY_SELECTED_CART_ID = "selected_cart_id"
    }

    override fun createObservable(requestParam: RequestParams?): Observable<CartListData> {
        val cartId = requestParam?.getString(PARAM_SELECTED_CART_ID, "") ?: ""
        val variables = mapOf(
                PARAM_KEY_LANG to PARAM_VALUE_ID,
                PARAM_KEY_SELECTED_CART_ID to cartId
        )

        val graphqlRequest = GraphqlRequest(queryString, ShopGroupSimplifiedGqlResponse::class.java, variables)
        val graphqlUseCase = GraphqlUseCase()
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        return graphqlUseCase.createObservable(RequestParams.EMPTY)
                .map {
                    val shopGroupSimplifiedGqlResponse = it.getData<ShopGroupSimplifiedGqlResponse>(ShopGroupSimplifiedGqlResponse::class.java)
                    if (shopGroupSimplifiedGqlResponse != null) {
                        if (shopGroupSimplifiedGqlResponse.shopGroupSimplifiedResponse.status == "OK") {
                            cartSimplifiedMapper.convertToCartItemDataList(shopGroupSimplifiedGqlResponse.shopGroupSimplifiedResponse.data)
                        } else {
                            if (shopGroupSimplifiedGqlResponse.shopGroupSimplifiedResponse.errorMessages.isNotEmpty()) {
                                throw CartResponseErrorException(shopGroupSimplifiedGqlResponse.shopGroupSimplifiedResponse.errorMessages.joinToString())
                            } else {
                                throw ResponseErrorException()
                            }
                        }
                    } else {
                        throw ResponseErrorException()
                    }
                }
                .subscribeOn(schedulers.io)
                .observeOn(schedulers.main)
    }
}